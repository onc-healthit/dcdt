package gov.hhs.onc.dcdt.service.wrapper;


import gov.hhs.onc.dcdt.service.ToolService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.tanukisoftware.wrapper.WrapperManager;
import org.tanukisoftware.wrapper.event.WrapperEvent;
import org.tanukisoftware.wrapper.event.WrapperEventListener;

public abstract class ToolServiceWrapper<T extends AbstractApplicationContext, U extends ToolService<T>> implements Runnable, WrapperEventListener {
    protected static class ToolServiceWrapperShutdownHook<T extends AbstractApplicationContext, U extends ToolService<T>> implements Runnable {
        private U service;

        public ToolServiceWrapperShutdownHook(U service) {
            this.service = service;
        }

        @Override
        public void run() {
            this.service.stop();
        }
    }

    protected Thread shutdownHookThread;
    protected String[] args;
    protected U service;
    protected Thread serviceThread;

    private final static long WRAPPER_EVENT_LISTENER_MASK = WrapperEventListener.EVENT_FLAG_SERVICE | WrapperEventListener.EVENT_FLAG_CONTROL;

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolServiceWrapper.class);

    protected ToolServiceWrapper() {
        this(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    protected ToolServiceWrapper(String ... args) {
        this.args = args;

        this.initialize();
    }

    public void start() {
        this.startWrapper();
        this.startService();
    }

    public void stop() {
        this.stopService();
        this.stopWrapper();
    }

    @Override
    public void run() {
        this.start();
    }

    public ToolServiceWrapperStatus status() {
        return ToolServiceWrapperUtils.getWrapperStatus();
    }

    @Override
    public void fired(WrapperEvent event) {
        LOGGER.info(ToolServiceWrapperUtils.getWrapperDisplayName() + " (class=" + this.getClass().getName() + ") event: " + event);
    }

    protected void startService() {
        this.serviceThread.start();
    }

    protected void startWrapper() {
        LOGGER.info(ToolServiceWrapperUtils.getWrapperDisplayName() + " (class=" + this.getClass().getName() + ") started: args=["
            + StringUtils.join(this.args) + "]");
    }

    protected void stopService() {
        this.serviceThread.interrupt();
    }

    protected void stopWrapper() {
        LOGGER.info(ToolServiceWrapperUtils.getWrapperDisplayName() + " (class=" + this.getClass().getName() + ") stopped.");
    }

    protected abstract U createService();

    protected void initializeService() {
        this.service = this.createService();
        this.serviceThread = new Thread(this.service);

        // Adding shutdown hooks
        this.shutdownHookThread = new Thread(new ToolServiceWrapperShutdownHook(this.service));
        Runtime.getRuntime().addShutdownHook(this.shutdownHookThread);
    }

    protected void initializeWrapper() {
        // Registering wrapper event listener
        WrapperManager.addWrapperEventListener(this, WRAPPER_EVENT_LISTENER_MASK);
    }

    protected void initialize() {
        this.initializeWrapper();
        this.initializeService();
    }
}
