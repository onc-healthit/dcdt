package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyTaggedIdentifier;
import org.bouncycastle.asn1.x509.KeyUsage;

public enum KeyUsageType implements CryptographyTaggedIdentifier {
    DIGITAL_SIGNATURE("digitalSignature", KeyUsage.digitalSignature), NON_REPUDIATION("nonRepudiation", KeyUsage.nonRepudiation), KEY_ENCIPHERMENT(
        "keyEncipherment", KeyUsage.keyEncipherment), DATA_ENCIPHERMENT("dataEncipherment", KeyUsage.dataEncipherment), KEY_AGREEMENT("keyAgreement",
        KeyUsage.keyAgreement), KEY_CERT_SIGN("keyCertSign", KeyUsage.keyCertSign), CRL_SIGN("cRLSign", KeyUsage.cRLSign), ENCIPHER_ONLY("encipherOnly",
        KeyUsage.encipherOnly), DECIPHER_ONLY("decipherOnly", KeyUsage.decipherOnly);

    private final String id;
    private final int tag;

    private KeyUsageType(String id, int tag) {
        this.id = id;
        this.tag = tag;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getTag() {
        return this.tag;
    }
}
