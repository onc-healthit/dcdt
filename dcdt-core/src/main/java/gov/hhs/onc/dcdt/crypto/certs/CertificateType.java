package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyContentTypes;
import gov.hhs.onc.dcdt.crypto.CryptographyObjectIdentifier;
import gov.hhs.onc.dcdt.beans.ToolTypeIdentifier;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.springframework.util.MimeType;

public enum CertificateType implements CryptographyObjectIdentifier, ToolTypeIdentifier {
    X509("X.509", PKCSObjectIdentifiers.x509Certificate, X509Certificate.class, CryptographyContentTypes.APP_PKIX_CERT,
        CryptographyContentTypes.APP_PKCS7_MIME, "cer", "p7c");

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final Class<?> type;
    private final MimeType contentType;
    private final MimeType collectionContentType;
    private final String fileNameExt;
    private final String collectionFileNameExt;

    private CertificateType(String id, ASN1ObjectIdentifier oid, Class<?> type, MimeType contentType, MimeType collectionContentType, String fileNameExt,
        String collectionFileNameExt) {
        this.id = id;
        this.oid = oid;
        this.type = type;
        this.contentType = contentType;
        this.collectionContentType = collectionContentType;
        this.fileNameExt = fileNameExt;
        this.collectionFileNameExt = collectionFileNameExt;
    }

    public MimeType getCollectionContentType() {
        return this.collectionContentType;
    }

    public String getCollectionFileNameExtension() {
        return this.collectionFileNameExt;
    }

    public MimeType getContentType() {
        return this.contentType;
    }

    public String getFileNameExtension() {
        return this.fileNameExt;
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
