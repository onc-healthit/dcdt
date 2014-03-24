package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyObjectIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyTypeIdentifier;
import gov.hhs.onc.dcdt.utils.ToolMimeTypeUtils;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.springframework.util.MimeType;

public enum CertificateType implements CryptographyObjectIdentifier, CryptographyTypeIdentifier {
    X509("X.509", PKCSObjectIdentifiers.x509Certificate, X509Certificate.class, new MimeType(ToolMimeTypeUtils.TYPE_APP, "x-x509-ca-cert"));

    private final String name;
    private final ASN1ObjectIdentifier oid;
    private final Class<?> type;
    private final MimeType contentType;

    private CertificateType(String name, ASN1ObjectIdentifier oid, Class<?> type, MimeType contentType) {
        this.name = name;
        this.oid = oid;
        this.type = type;
        this.contentType = contentType;
    }

    public MimeType getContentType() {
        return this.contentType;
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
