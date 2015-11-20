package gov.hhs.onc.dcdt.mail;

public final class MailHeaders {
    public final static String CONTENT_PREFIX = "Content-";

    public final static String CONTENT_TYPE_NAME = CONTENT_PREFIX + "Type";
    public final static String CONTENT_TRANSFER_ENCODING_NAME = CONTENT_PREFIX + "Transfer-Encoding";
    public final static String DATE_NAME = "Date";
    public final static String FROM_NAME = "From";
    public final static String ORIG_DATE_NAME = "Orig-" + DATE_NAME;
    public final static String REFERENCES_NAME = "References";
    public final static String REPLY_TO_NAME = "Reply-To";

    private MailHeaders() {
    }
}
