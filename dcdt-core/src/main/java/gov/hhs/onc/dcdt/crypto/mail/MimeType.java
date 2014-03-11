package gov.hhs.onc.dcdt.crypto.mail;

public enum MimeType {
    PKCS7("application/pkcs7-mime"), X_PKCS7("application/x-pkcs7-mime"), PKCS7_SIGNATURE("application/pkcs7-signature"), X_PKCS7_SIGNATURE(
        "application/x-pkcs7-signature"), MULTIPART_SIGNED("multipart/signed");

    private final String type;

    private MimeType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
