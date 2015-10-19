package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithmIdentifier;
import gov.hhs.onc.dcdt.crypto.DigestAlgorithm;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public enum SignatureAlgorithm implements CryptographyAlgorithmIdentifier {
    SHA1_WITH_RSA_ENCRYPTION("sha1WithRSAEncryption", PKCSObjectIdentifiers.sha1WithRSAEncryption, DigestAlgorithm.SHA1),
    SHA256_WITH_RSA_ENCRYPTION("sha256WithRSAEncryption", PKCSObjectIdentifiers.sha256WithRSAEncryption, DigestAlgorithm.SHA256),
    SHA384_WITH_RSA_ENCRYPTION("sha384WithRSAEncryption", PKCSObjectIdentifiers.sha384WithRSAEncryption, DigestAlgorithm.SHA384),
    SHA512_WITH_RSA_ENCRYPTION("sha512WithRSAEncryption", PKCSObjectIdentifiers.sha512WithRSAEncryption, DigestAlgorithm.SHA512);

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final AlgorithmIdentifier algId;
    private final DigestAlgorithm digestAlg;

    private SignatureAlgorithm(String id, ASN1ObjectIdentifier oid, DigestAlgorithm digestAlg) {
        this.id = id;
        this.oid = oid;
        this.algId = CryptographyUtils.SIG_ALG_ID_FINDER.find(this.id);
        this.digestAlg = digestAlg;
    }

    @Override
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }

    public DigestAlgorithm getDigestAlgorithm() {
        return this.digestAlg;
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
