package gov.hhs.onc.dcdt.mail.crypto;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithmIdentifier;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Set;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;

public enum MailDigestAlgorithm implements CryptographyAlgorithmIdentifier {
    SHA1("SHA-1", OIWObjectIdentifiers.idSHA1, "sha1", "sha-1"), SHA256("SHA-256", NISTObjectIdentifiers.id_sha256, "sha256", "sha-256"), SHA384("SHA-384",
        NISTObjectIdentifiers.id_sha384, "sha384", "sha-384"), SHA512("SHA-512", NISTObjectIdentifiers.id_sha512, "sha512", "sha-512");

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final Set<String> micalg;
    private final AlgorithmIdentifier algId;

    private MailDigestAlgorithm(String id, ASN1ObjectIdentifier oid, String ... micalg) {
        this.id = id;
        this.oid = oid;
        this.micalg = ToolArrayUtils.asSet(micalg);
        this.algId = new DefaultDigestAlgorithmIdentifierFinder().find(this.id);
    }

    @Override
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public Set<String> getMicalg() {
        return this.micalg;
    }

    @Override
    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }
}
