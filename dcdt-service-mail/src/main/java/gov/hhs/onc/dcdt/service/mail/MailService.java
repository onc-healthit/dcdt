package gov.hhs.onc.dcdt.service.mail;

import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import gov.hhs.onc.dcdt.service.mail.server.MailServer;

public interface MailService extends ToolService<MailServerConfig, MailServer<MailServerConfig>> {
}
