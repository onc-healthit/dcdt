package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyObjectIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyTypeIdentifier;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.springframework.util.MimeType;

public enum CertificateType implements CryptographyObjectIdentifier, CryptographyTypeIdentifier {
    X509("X.509", PKCSObjectIdentifiers.x509Certificate, X509Certificate.class, new MimeType(ToolMimeTypeUtils.TYPE_APP, "x-x509-ca-cert"));

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final Class<?> type;
    private final MimeType contentType;

    private CertificateType(String id, ASN1ObjectIdentifier oid, Class<?> type, MimeType contentType) {
        this.id = id;
        this.oid = oid;
        this.type = type;
        this.contentType = contentType;
    }

    public MimeType getContentType() {
        return this.contentType;
    }

    @Override
    public String getId() {
        return this.id;
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
