package gov.hhs.onc.dcdt.crypto.mail;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public enum MailCipherAlgorithm implements CryptographyAlgorithmIdentifier {
    AES128("AES128", NISTObjectIdentifiers.id_aes128_CBC), AES256("AES256", NISTObjectIdentifiers.id_aes256_CBC);

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final AlgorithmIdentifier algId;

    private MailCipherAlgorithm(String id, ASN1ObjectIdentifier oid) {
        this.id = id;
        this.oid = oid;
        this.algId = new AlgorithmIdentifier(this.oid);
    }

    @Override
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }
}