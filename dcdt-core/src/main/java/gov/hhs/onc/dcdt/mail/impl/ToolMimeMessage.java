package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.mail.ToolMailException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class ToolMimeMessage extends MimeMessage {
    public ToolMimeMessage(Session session, byte ... content) throws MessagingException {
        this(session);

        try (InputStream contentInStream = new ByteArrayInputStream(content)) {
            this.parse(contentInStream);
        } catch (IOException e) {
            throw new ToolMailException("Unable to parse mail MIME message.", e);
        }
    }

    public ToolMimeMessage(Session session) throws MessagingException {
        super(session);
    }

    @Override
    public void updateMessageID() throws MessagingException {
        this.updateMessageID(false);
    }

    public void updateMessageID(boolean force) throws MessagingException {
        if ((this.getMessageID() == null) || force) {
            super.updateMessageID();
        }
    }

    @Override
    protected ToolMimeMessage createMimeMessage(Session session) throws MessagingException {
        return new ToolMimeMessage(session);
    }
}
