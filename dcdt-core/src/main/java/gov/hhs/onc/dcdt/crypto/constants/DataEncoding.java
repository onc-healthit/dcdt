package gov.hhs.onc.dcdt.crypto.constants;

public enum DataEncoding {
    DER("der"), PEM("pem");

    private final String encoding;

    private DataEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return this.encoding;
    }

}
