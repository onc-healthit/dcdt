package gov.hhs.onc.dcdt.service.mail.wrapper;

import gov.hhs.onc.dcdt.service.mail.MailService;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;

public class MailServiceWrapper extends ToolServiceWrapper<MailService> {
    public MailServiceWrapper() {
        super();
    }

    public MailServiceWrapper(String ... args) {
        super(args);
    }

    public static void main(String ... args) {
        new MailServiceWrapper(args).start();
    }

    @Override
    protected MailService createService() {
        return new MailService();
    }
}
