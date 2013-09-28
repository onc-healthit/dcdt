package gov.hhs.onc.dcdt.standalone;


import gov.hhs.onc.dcdt.standalone.utils.ToolWrapperUtils;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tanukisoftware.wrapper.WrapperManager;
import org.tanukisoftware.wrapper.event.WrapperEvent;
import org.tanukisoftware.wrapper.event.WrapperEventListener;

public abstract class ToolWrapper<T extends AbstractApplicationContext> implements Runnable, WrapperEventListener {
    protected static class ToolWrapperShutdownHook implements Runnable {
        private ToolWrapper wrapper;

        public ToolWrapperShutdownHook(ToolWrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        public void run() {
            this.wrapper.stop();
        }
    }

    protected String[] args;
    protected AbstractApplicationContext parentContext;
    protected List<String> contextConfigLocs;
    protected T context;
    protected AutowireCapableBeanFactory beanFactory;
    protected Thread shutdownHookThread;

    private final static List<String> CONTEXT_CONFIG_LOCS_CORE = ToolResourceUtils.getOverrideableResourceLocation("spring/spring-core.xml");

    private final static long WRAPPER_EVENT_LISTENER_MASK = WrapperEventListener.EVENT_FLAG_SERVICE | WrapperEventListener.EVENT_FLAG_CONTROL;

    private final static long SERVICE_THREAD_SLEEP_TIME_MS = 1000L;

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolWrapper.class);

    {
        this.initialize();

        if (this.isStandalone()) {
            this.initializeStandalone();
        } else {
            this.initializeEmbedded();
        }

        this.initializeContext();
    }

    protected ToolWrapper() {
    }

    protected ToolWrapper(String[] args) {
        this.args = args;
    }

    protected ToolWrapper(AbstractApplicationContext parentContext) {
        this.parentContext = parentContext;
    }

    public void start() {
        this.startContext();
        this.startWrapper();
        this.startService();
    }

    public void stop() {
        this.stopContext();
        this.stopWrapper();
        this.stopService();
    }

    public ToolWrapperStatus status() {
        return ToolWrapperUtils.getWrapperStatus();
    }

    @Override
    public void fired(WrapperEvent event) {
        LOGGER.info(ToolWrapperUtils.getWrapperDisplayName() + " (class=" + this.getClass().getName() + ") event: " + event);
    }

    @Override
    public void run() {
        this.start();
    }

    protected void startContext() {
        this.context.refresh();
        this.context.start();

        this.beanFactory = this.context.getAutowireCapableBeanFactory();
        this.beanFactory.autowireBean(this);
    }

    protected void startWrapper() {
    }

    protected void startService() {
        LOGGER.info(ToolWrapperUtils.getWrapperDisplayName() + " (class=" + this.getClass().getName() + ") started: args=[" + StringUtils.join(this.args) + "]");

        Thread currentThread;

        while((currentThread = Thread.currentThread()).isAlive() && !currentThread.isInterrupted()) {
            try {
                Thread.sleep(SERVICE_THREAD_SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                LOGGER.debug(ToolWrapperUtils.getWrapperDisplayName() + " (class=" + this.getClass().getName() + ") thread interrupted.");
            }
        }
    }

    protected void stopContext() {
        if ((this.context != null) && this.context.isRunning()) {
            this.context.stop();
        }
    }

    protected void stopWrapper() {
    }

    protected void stopService() {
        LOGGER.info(ToolWrapperUtils.getWrapperDisplayName() + " (class=" + this.getClass().getName() + ") stopped.");
    }

    protected T createContext() {
        return (T) new ClassPathXmlApplicationContext(this.contextConfigLocs.toArray(new String[this.contextConfigLocs.size()]), false, this.parentContext);
    }

    protected void initializeContext() {
        this.context = this.createContext();

        if (this.isStandalone()) {
            this.context.registerShutdownHook();
        }
    }

    protected void initializeStandalone() {
        // Registering wrapper event listener
        WrapperManager.addWrapperEventListener(this, WRAPPER_EVENT_LISTENER_MASK);

        // Adding shutdown hooks
        this.shutdownHookThread = new Thread(new ToolWrapperShutdownHook(this));
        Runtime.getRuntime().addShutdownHook(this.shutdownHookThread);

        this.contextConfigLocs.addAll(0, CONTEXT_CONFIG_LOCS_CORE);
    }

    protected void initializeEmbedded() {
    }

    protected void initialize() {
        this.contextConfigLocs = new ArrayList<>();
    }

    protected boolean isStandalone() {
        return this.parentContext == null;
    }

    protected boolean isEmbedded() {
        return !this.isStandalone();
    }
}
