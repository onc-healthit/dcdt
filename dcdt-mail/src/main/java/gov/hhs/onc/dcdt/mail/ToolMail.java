package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.standalone.ToolWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolMail extends ToolWrapper {
    private final static Logger LOGGER = LoggerFactory.getLogger(ToolMail.class);

    public ToolMail(String ... args) {
        super(args);
    }

    public static void main(String ... args) {
        new ToolMail(args).start();
    }

    @Override
    public void start() {
        // TODO: implement

        super.start();
    }

    @Override
    public void stop() {
        // TODO: implement

        super.stop();
    }
}
