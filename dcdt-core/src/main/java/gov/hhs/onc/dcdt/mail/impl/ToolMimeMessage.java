package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.mail.ToolMailException;
import gov.hhs.onc.dcdt.mail.utils.ToolMimePartUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import io.netty.util.CharsetUtil;
import java.io.ByteArrayInputStream;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class ToolMimeMessage extends MimeMessage {
    private byte[] data;
    private String dataStr;

    public ToolMimeMessage(Session session, byte ... data) throws MessagingException {
        super(session);

        if (data.length == 0) {
            return;
        }

        this.modified = false;

        try (ByteArrayInputStream dataInStream = new ByteArrayInputStream(data)) {
            this.parse(dataInStream);
        } catch (Exception e) {
            throw new ToolMailException("Unable to parse mail MIME message.", e);
        }

        this.dataStr = new String((this.data = data), CharsetUtil.US_ASCII);
        this.saved = true;
    }

    @Override
    public void saveChanges() throws MessagingException {
        this.saveChanges(false);
    }

    public void saveChanges(boolean overrideMsgId) throws MessagingException {
        if (overrideMsgId) {
            this.updateMessageID(true);
        }

        super.saveChanges();

        this.updateData();
    }

    @Override
    public String toString() {
        return Objects.toString(this.dataStr);
    }

    @Override
    protected void updateMessageID() throws MessagingException {
        this.updateMessageID(false);
    }

    protected void updateMessageID(boolean override) throws MessagingException {
        if ((this.getMessageID() == null) || override) {
            super.updateMessageID();
        }
    }

    @Override
    protected ToolMimeMessage createMimeMessage(Session session) throws MessagingException {
        return new ToolMimeMessage(session);
    }

    private void updateData() throws MessagingException {
        try {
            this.dataStr = new String((this.data = ToolMimePartUtils.write(this)), CharsetUtil.US_ASCII);
        } catch (Exception e) {
            throw new ToolMailException(String.format("Unable to update mail MIME message (id=%s, from=%s, to=%s) data.", this.getMessageID(),
                ToolArrayUtils.getFirst(this.getFrom()), ToolArrayUtils.getFirst(this.getRecipients(Message.RecipientType.TO))), e);
        }
    }

    @Nullable
    public byte[] getData() {
        return this.data;
    }
}
