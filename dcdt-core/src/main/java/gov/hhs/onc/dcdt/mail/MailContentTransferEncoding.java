package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;

public enum MailContentTransferEncoding implements ToolIdentifier {
    SEVEN_BIT("7bit"), EIGHT_BIT("8bit"), BASE64("base64"), BINARY("binary"), QUOTED_PRINTABLE("quoted-printable");

    private final String id;

    private MailContentTransferEncoding(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
