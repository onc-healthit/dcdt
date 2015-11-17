package gov.hhs.onc.dcdt.mail.smtp.impl;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import gov.hhs.onc.dcdt.mail.smtp.SmtpMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public abstract class AbstractSmtpMessage<T extends Enum<T> & ToolIdentifier> implements SmtpMessage<T> {
    protected T type;

    protected AbstractSmtpMessage(T type) {
        this.type = type;
    }

    @Override
    public ByteBuf toBuffer() {
        return Unpooled.wrappedBuffer(this.toString().getBytes(CharsetUtil.US_ASCII));
    }

    @Override
    public T getType() {
        return this.type;
    }

    @Override
    public void setType(T type) {
        this.type = type;
    }
}
