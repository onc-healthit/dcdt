package gov.hhs.onc.dcdt.service.mail.smtp;

import gov.hhs.onc.dcdt.mail.smtp.SmtpTransportProtocol;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.server.MailServer;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandProcessor;
import java.util.Map;

public interface SmtpServer extends MailServer<SmtpTransportProtocol, SmtpServerConfig> {
    public Map<SmtpCommandType, SmtpCommandProcessor<?>> getCommandProcessors();
}
