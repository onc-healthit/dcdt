package gov.hhs.onc.dcdt.ldap;

import gov.hhs.onc.dcdt.standalone.ToolWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolLdap extends ToolWrapper {
    private final static Logger LOGGER = LoggerFactory.getLogger(ToolLdap.class);

    public ToolLdap(String ... args) {
        super(args);
    }

    public static void main(String ... args) {
        new ToolLdap(args).start();
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
