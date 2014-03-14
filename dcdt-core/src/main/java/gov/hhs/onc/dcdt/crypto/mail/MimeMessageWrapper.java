package gov.hhs.onc.dcdt.crypto.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class MimeMessageWrapper extends MimeMessage {
    private String messageId;

    public MimeMessageWrapper(MimeMessage mimeMessage) throws MessagingException {
        super(mimeMessage);
    }

    @Override
    public void updateMessageID() throws MessagingException {
        setHeader("Message-ID", this.messageId);
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
