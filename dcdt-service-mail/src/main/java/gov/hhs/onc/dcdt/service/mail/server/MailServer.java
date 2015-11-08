package gov.hhs.onc.dcdt.service.mail.server;

import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import gov.hhs.onc.dcdt.service.server.ToolChannelServer;

public interface MailServer<T extends MailServerConfig> extends ToolChannelServer<T> {
}
