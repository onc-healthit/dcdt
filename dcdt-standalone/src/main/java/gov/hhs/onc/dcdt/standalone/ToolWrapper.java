package gov.hhs.onc.dcdt.standalone;

import gov.hhs.onc.dcdt.standalone.utils.ToolWrapperUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanukisoftware.wrapper.WrapperManager;
import org.tanukisoftware.wrapper.event.WrapperEvent;
import org.tanukisoftware.wrapper.event.WrapperEventListener;

public abstract class ToolWrapper implements Runnable, WrapperEventListener {
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

    private final static long WRAPPER_EVENT_LISTENER_MASK = WrapperEventListener.EVENT_FLAG_SERVICE | WrapperEventListener.EVENT_FLAG_CONTROL;

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolWrapper.class);

    protected ToolWrapper(String ... args) {
        this.args = args;

        this.initialize();
    }

    public void start() {
        LOGGER.info(ToolWrapperUtils.getWrapperDisplayName() + " (class=" + this.getClass().getName() + ") started: args=[" + StringUtils.join(this.args) + "]");
    }

    public void stop() {
        LOGGER.info(ToolWrapperUtils.getWrapperDisplayName() + " (class=" + this.getClass().getName() + ") stopped.");
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

    protected void initialize() {
        // Registering wrapper event listener
        WrapperManager.addWrapperEventListener(this, WRAPPER_EVENT_LISTENER_MASK);

        // Adding shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(new ToolWrapperShutdownHook(this)));
    }
}
