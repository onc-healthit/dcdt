package gov.hhs.onc.dcdt.crypto.constants;

public enum CertificateType {
    X509("X.509");

    private final String type;

    private CertificateType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

}
