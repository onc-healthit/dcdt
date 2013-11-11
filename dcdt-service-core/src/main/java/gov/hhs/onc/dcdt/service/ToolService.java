package gov.hhs.onc.dcdt.service;


import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class ToolService<T extends AbstractApplicationContext> implements Runnable {
    protected ApplicationContext parentContext;
    protected List<String> contextConfigLocs;
    protected T context;
    protected AutowireCapableBeanFactory beanFactory;
    protected boolean running;

    private final static List<String> CONTEXT_CONFIG_LOCS_CORE = ToolResourceUtils.getOverrideableResourceLocations(ToolArrayUtils.asList(
        "spring/spring-core*.xml", "spring/spring-service.xml", "spring/spring-service-embedded.xml", "spring/spring-service-standalone.xml"));

    private final static long SERVICE_THREAD_SLEEP_TIME_MS = 1000L;

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolService.class);

    protected ToolService() {
        this(null);
    }

    protected ToolService(ApplicationContext parentContext) {
        this.parentContext = parentContext;

        this.initialize();
    }

    public void start() {
        this.startContext();
        this.startService();
    }

    public void stop() {
        this.stopService();
        this.stopContext();

        this.running = false;
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

    protected void startService() {
        Thread currentThread;

        while((currentThread = Thread.currentThread()).isAlive() && !currentThread.isInterrupted()) {
            this.running = true;

            try {
                Thread.sleep(SERVICE_THREAD_SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                LOGGER.debug("Tool service (class=" + this.getClass().getName() + ") thread interrupted.");
            }
        }
    }

    protected void stopContext() {
        if ((this.context != null) && this.context.isRunning()) {
            this.context.stop();
        }
    }

    protected void stopService() {
        Thread.currentThread().interrupt();
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
        this.contextConfigLocs.addAll(0, CONTEXT_CONFIG_LOCS_CORE);
    }

    protected void initializeEmbedded() {
    }

    protected void initialize() {
        this.contextConfigLocs = new ArrayList<>();

        if (this.isStandalone()) {
            this.initializeStandalone();
        } else {
            this.initializeEmbedded();
        }

        this.initializeContext();
    }

    protected boolean isStandalone() {
        return this.parentContext == null;
    }

    protected boolean isEmbedded() {
        return !this.isStandalone();
    }

    public boolean isRunning() {
        return this.running;
    }
}
