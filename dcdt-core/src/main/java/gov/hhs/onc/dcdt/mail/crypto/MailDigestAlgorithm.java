package gov.hhs.onc.dcdt.mail.crypto;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;

public enum MailDigestAlgorithm implements CryptographyAlgorithmIdentifier {
    SHA1("SHA-1", OIWObjectIdentifiers.idSHA1, "sha1"), SHA256("SHA-256", NISTObjectIdentifiers.id_sha256, "sha256");

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final String micalg;
    private final AlgorithmIdentifier algId;

    private MailDigestAlgorithm(String id, ASN1ObjectIdentifier oid, String micalg) {
        this.id = id;
        this.oid = oid;
        this.micalg = micalg;
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

    public String getMicalg() {
        return this.micalg;
    }

    @Override
    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }
}
