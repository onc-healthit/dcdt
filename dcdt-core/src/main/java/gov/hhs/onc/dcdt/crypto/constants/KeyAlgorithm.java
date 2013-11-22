package gov.hhs.onc.dcdt.crypto.constants;

public enum KeyAlgorithm {
    RSA("RSA");

    private final String algorithm;

    private KeyAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }
}
