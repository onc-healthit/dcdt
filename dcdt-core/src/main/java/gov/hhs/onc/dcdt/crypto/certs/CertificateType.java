package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyObjectIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyTypeIdentifier;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public enum CertificateType implements CryptographyObjectIdentifier, CryptographyTypeIdentifier {
    X509("X.509", PKCSObjectIdentifiers.x509Certificate, X509Certificate.class);

    private final String name;
    private final ASN1ObjectIdentifier oid;
    private final Class<?> type;

    private CertificateType(String name, ASN1ObjectIdentifier oid, Class<?> type) {
        this.name = name;
        this.oid = oid;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }
}
