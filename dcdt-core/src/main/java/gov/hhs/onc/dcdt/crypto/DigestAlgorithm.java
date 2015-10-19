package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public enum DigestAlgorithm implements CryptographyAlgorithmIdentifier {
    SHA1("SHA-1", OIWObjectIdentifiers.idSHA1, "sha1", "sha-1"), SHA256("SHA-256", NISTObjectIdentifiers.id_sha256, "sha256", "sha-256"),
    SHA384("SHA-384", NISTObjectIdentifiers.id_sha384, "sha384", "sha-384"), SHA512("SHA-512", NISTObjectIdentifiers.id_sha512, "sha512", "sha-512");

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final Set<String> micalgs;
    private final AlgorithmIdentifier algId;

    private DigestAlgorithm(String id, ASN1ObjectIdentifier oid, String ... micalgs) {
        this.id = id;
        this.oid = oid;
        this.micalgs = new LinkedHashSet<>(Arrays.asList(micalgs));
        this.algId = CryptographyUtils.DIGEST_ALG_ID_FINDER.find(this.id);
    }

    @Override
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public Set<String> getMicalgs() {
        return this.micalgs;
    }

    @Override
    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }
}
