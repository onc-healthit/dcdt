package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyObjectIdentifier;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public enum CertificateType implements CryptographyObjectIdentifier {
    X509("X.509", PKCSObjectIdentifiers.x509Certificate);

    private final String name;
    private final ASN1ObjectIdentifier oid;

    private CertificateType(String name, ASN1ObjectIdentifier oid) {
        this.name = name;
        this.oid = oid;
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
