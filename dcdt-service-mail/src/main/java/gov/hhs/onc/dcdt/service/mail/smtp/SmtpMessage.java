package gov.hhs.onc.dcdt.service.mail.smtp;

import gov.hhs.onc.dcdt.service.mail.MailProtocolMessage;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nullable;

public interface SmtpMessage extends MailProtocolMessage {
    public ByteBuf toBuffer();

    public boolean hasId();

    @Nullable
    public String getId();
}
