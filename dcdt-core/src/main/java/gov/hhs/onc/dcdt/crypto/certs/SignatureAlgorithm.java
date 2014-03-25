package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;

public enum SignatureAlgorithm implements CryptographyAlgorithmIdentifier {
    SHA1WITHRSA("SHA1WITHRSA", PKCSObjectIdentifiers.sha1WithRSAEncryption);

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final AlgorithmIdentifier algId;
    private final AlgorithmIdentifier digestAlgoId;

    private SignatureAlgorithm(String id, ASN1ObjectIdentifier oid) {
        this.id = id;
        this.oid = oid;
        this.algId = new DefaultSignatureAlgorithmIdentifierFinder().find(this.id);
        this.digestAlgoId = new DefaultDigestAlgorithmIdentifierFinder().find(this.algId);
    }

    @Override
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }

    public AlgorithmIdentifier getDigestAlgorithmId() {
        return this.digestAlgoId;
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
