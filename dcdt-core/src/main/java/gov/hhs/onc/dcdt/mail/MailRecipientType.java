package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;

public enum MailRecipientType implements ToolIdentifier {
    BCC(RecipientType.BCC), CC(RecipientType.CC), NEWSGROUPS(MimeMessage.RecipientType.NEWSGROUPS), TO(RecipientType.TO);

    private final RecipientType type;

    private MailRecipientType(RecipientType type) {
        this.type = type;
    }

    @Override
    public String getId() {
        return this.type.toString();
    }

    public RecipientType getType() {
        return this.type;
    }
}
