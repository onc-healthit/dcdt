package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.NoopCommand;
import org.springframework.stereotype.Component;

@Component("smtpCmdProcNoop")
public class NoopCommandProcessor extends AbstractSmtpCommandProcessor<NoopCommand> {
    public NoopCommandProcessor() {
        super(SmtpCommandType.NOOP);
    }
}
