package gov.hhs.onc.dcdt.mail.crypto;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public enum MailEncryptionAlgorithm implements CryptographyAlgorithmIdentifier {
    AES128("AES128", NISTObjectIdentifiers.id_aes128_CBC), AES192("AES192", NISTObjectIdentifiers.id_aes192_CBC), AES256("AES256",
        NISTObjectIdentifiers.id_aes256_CBC), DES_EDE3("DESEDE", PKCSObjectIdentifiers.des_EDE3_CBC);

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final AlgorithmIdentifier algId;

    private MailEncryptionAlgorithm(String id, ASN1ObjectIdentifier oid) {
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