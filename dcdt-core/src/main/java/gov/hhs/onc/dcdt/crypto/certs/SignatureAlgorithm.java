package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;

public enum SignatureAlgorithm implements CryptographyAlgorithmIdentifier {
    SHA1_WITH_RSA_ENCRYPTION("sha1WithRSAEncryption", PKCSObjectIdentifiers.sha1WithRSAEncryption), SHA256_WITH_RSA_ENCRYPTION("sha256WithRSAEncryption",
        PKCSObjectIdentifiers.sha256WithRSAEncryption), SHA384_WITH_RSA_ENCRYPTION("sha384WithRSAEncryption", PKCSObjectIdentifiers.sha384WithRSAEncryption),
    SHA512_WITH_RSA_ENCRYPTION("sha512WithRSAEncryption", PKCSObjectIdentifiers.sha512WithRSAEncryption);

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final AlgorithmIdentifier algId;
    private final AlgorithmIdentifier digestAlgId;

    private SignatureAlgorithm(String id, ASN1ObjectIdentifier oid) {
        this.id = id;
        this.oid = oid;
        this.algId = new DefaultSignatureAlgorithmIdentifierFinder().find(this.id);
        this.digestAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(this.algId);
    }

    @Override
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }

    public AlgorithmIdentifier getDigestAlgorithmId() {
        return this.digestAlgId;
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
