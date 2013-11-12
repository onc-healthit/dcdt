package gov.hhs.onc.dcdt.service.mail.wrapper;


import gov.hhs.onc.dcdt.service.mail.ToolMailService;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;

public class ToolMailServiceWrapper extends ToolServiceWrapper<ToolMailService> {
    public ToolMailServiceWrapper() {
        super();
    }

    public ToolMailServiceWrapper(String ... args) {
        super(args);
    }

    public static void main(String ... args) {
        new ToolMailServiceWrapper(args).start();
    }

    @Override
    protected ToolMailService createService() {
        return new ToolMailService();
    }
}
