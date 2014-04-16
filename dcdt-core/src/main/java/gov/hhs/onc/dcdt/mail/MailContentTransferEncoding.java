package gov.hhs.onc.dcdt.mail;

public enum MailContentTransferEncoding {
    SEVEN_BIT("7bit"), BASE64("base64"), QUOTED("quoted-printable");

    private final String value;

    private MailContentTransferEncoding(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
