package gov.hhs.onc.dcdt.crypto.constants;

public enum PemType {
    PKCS8_PRIVATE_KEY("RSA PRIVATE KEY"), X509_CERTIFICATE("X.509 CERTIFICATE");

    private final String type;

    private PemType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
