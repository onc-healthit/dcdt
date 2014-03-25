package gov.hhs.onc.dcdt.crypto;

public enum DataEncoding {
    DER("der", ".der"), PEM("pem", ".pem");

    private final String encoding;
    private final String fileExt;

    private DataEncoding(String encoding, String fileExt) {
        this.encoding = encoding;
        this.fileExt = fileExt;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public String getFileExtension() {
        return this.fileExt;
    }
}
