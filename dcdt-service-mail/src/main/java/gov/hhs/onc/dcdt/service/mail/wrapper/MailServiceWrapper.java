package gov.hhs.onc.dcdt.service.mail.wrapper;

import gov.hhs.onc.dcdt.net.TransportProtocol;
import gov.hhs.onc.dcdt.service.mail.MailService;
import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import gov.hhs.onc.dcdt.service.mail.server.MailServer;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;

public interface MailServiceWrapper extends
    ToolServiceWrapper<TransportProtocol, MailServerConfig<TransportProtocol>, MailServer<TransportProtocol, MailServerConfig<TransportProtocol>>, MailService> {
}
