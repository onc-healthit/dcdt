package gov.hhs.onc.dcdt.service.mail.wrapper;

import gov.hhs.onc.dcdt.service.mail.MailService;
import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import gov.hhs.onc.dcdt.service.mail.server.MailServer;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;

public interface MailServiceWrapper extends ToolServiceWrapper<MailServerConfig, MailServer<MailServerConfig>, MailService> {
}
