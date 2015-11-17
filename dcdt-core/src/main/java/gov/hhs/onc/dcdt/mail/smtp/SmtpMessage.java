package gov.hhs.onc.dcdt.mail.smtp;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import io.netty.buffer.ByteBuf;

public interface SmtpMessage<T extends Enum<T> & ToolIdentifier> {
    public ByteBuf toBuffer();

    public T getType();

    public void setType(T type);
}
