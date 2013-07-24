package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.standalone.ToolWrapper;
import gov.hhs.onc.dcdt.standalone.utils.ToolWrapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolDns extends ToolWrapper {
    // TEMP: dev
    private final static long MS_IN_SECS = 1000L;

    // TEMP: dev
    private final static long SLEEP_TIME_MS = 5000L;

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolDns.class);

    // TEMP: dev
    private int sleepTime = 0;

    public ToolDns(String ... args) {
        super(args);
    }

    public static void main(String ... args) {
        new ToolDns(args).start();
    }

    @Override
    public void start() {
        super.start();

        // TEMP: dev
        while(true) {
            try {
                Thread.sleep(SLEEP_TIME_MS);

                this.sleepTime += SLEEP_TIME_MS;

                LOGGER.trace(ToolWrapperUtils.getWrapperDisplayName() + " (class=" + this.getClass().getName() + ") slept: " + (this.sleepTime / MS_IN_SECS)
                        + "s");
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void stop() {
        // TODO: implement

        super.stop();
    }
}
