package gov.hhs.onc.dcdt.service.mail.wrapper.impl;

import gov.hhs.onc.dcdt.service.mail.MailService;
import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import gov.hhs.onc.dcdt.service.mail.impl.MailServiceImpl;
import gov.hhs.onc.dcdt.service.mail.server.MailServer;
import gov.hhs.onc.dcdt.service.mail.wrapper.MailServiceWrapper;
import gov.hhs.onc.dcdt.service.wrapper.impl.AbstractToolServiceWrapper;

public class MailServiceWrapperImpl extends AbstractToolServiceWrapper<MailServerConfig, MailServer<MailServerConfig>, MailService> implements
    MailServiceWrapper {
    public MailServiceWrapperImpl(String ... args) {
        super(MailService.class, MailServiceImpl.class, args);
    }

    public static void main(String ... args) {
        new MailServiceWrapperImpl(args).start();
    }
}
