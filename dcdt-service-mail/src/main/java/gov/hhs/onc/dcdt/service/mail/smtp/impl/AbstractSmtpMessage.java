package gov.hhs.onc.dcdt.service.mail.smtp.impl;

import gov.hhs.onc.dcdt.service.mail.MailProtocolMessageDirection;
import gov.hhs.onc.dcdt.service.mail.impl.AbstractMailProtocolMessage;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractSmtpMessage extends AbstractMailProtocolMessage implements SmtpMessage {
    protected String id;

    protected AbstractSmtpMessage(MailProtocolMessageDirection direction, String id) {
        super(direction);

        this.id = id;
    }

    @Override
    public ByteBuf toBuffer() {
        return Unpooled.wrappedBuffer(this.toString().getBytes(CharsetUtil.US_ASCII));
    }

    @Override
    public boolean hasId() {
        return !StringUtils.isEmpty(this.id);
    }

    @Nullable
    @Override
    public String getId() {
        return this.id;
    }
}
