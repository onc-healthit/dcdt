package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyContentTypes;
import gov.hhs.onc.dcdt.crypto.CryptographyObjectIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyTypeIdentifier;
import java.security.cert.X509CRL;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.springframework.util.MimeType;

public enum CrlType implements CryptographyObjectIdentifier, CryptographyTypeIdentifier {
    X509("X.509", PKCSObjectIdentifiers.x509Crl, X509CRL.class, CryptographyContentTypes.APP_PKIX_CRL, CryptographyContentTypes.APP_PKCS7_MIME, "crl", "p7c");

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final Class<?> type;
    private final MimeType contentType;
    private final MimeType collectionContentType;
    private final String fileNameExt;
    private final String collectionFileNameExt;

    private CrlType(String id, ASN1ObjectIdentifier oid, Class<?> type, MimeType contentType, MimeType collectionContentType, String fileNameExt,
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
