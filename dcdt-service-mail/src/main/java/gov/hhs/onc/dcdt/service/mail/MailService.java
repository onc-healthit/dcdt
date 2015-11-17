package gov.hhs.onc.dcdt.service.mail;

import gov.hhs.onc.dcdt.net.TransportProtocol;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import gov.hhs.onc.dcdt.service.mail.server.MailServer;

public interface MailService extends
    ToolService<TransportProtocol, MailServerConfig<TransportProtocol>, MailServer<TransportProtocol, MailServerConfig<TransportProtocol>>> {
}
