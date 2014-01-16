package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithm;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;

public enum SignatureAlgorithm implements CryptographyAlgorithm {
    SHA1WITHRSA("SHA1WITHRSA", PKCSObjectIdentifiers.sha1WithRSAEncryption);

    private final String name;
    private final ASN1ObjectIdentifier oid;
    private final AlgorithmIdentifier id;
    private final AlgorithmIdentifier digestId;

    private SignatureAlgorithm(String name, ASN1ObjectIdentifier oid) {
        this.name = name;
        this.oid = oid;
        this.id = new DefaultSignatureAlgorithmIdentifierFinder().find(this.name);
        this.digestId = new DefaultDigestAlgorithmIdentifierFinder().find(this.id);
    }

    public AlgorithmIdentifier getDigestId() {
        return this.digestId;
    }

    @Override
    public AlgorithmIdentifier getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }
}
