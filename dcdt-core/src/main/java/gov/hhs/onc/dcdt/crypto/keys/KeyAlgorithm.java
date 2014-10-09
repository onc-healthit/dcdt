package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithmIdentifier;
import gov.hhs.onc.dcdt.net.mime.CoreContentTypes;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.springframework.util.MimeType;

public enum KeyAlgorithm implements CryptographyAlgorithmIdentifier {
    RSA("RSA", PKCSObjectIdentifiers.rsaEncryption, "X.509", X509EncodedKeySpec.class, "PKCS#8", PKCS8EncodedKeySpec.class, 512, new MimeType(
        CoreContentTypes.APP_TYPE, "pkcs8"));

    private final String id;
    private final ASN1ObjectIdentifier oid;
    private final AlgorithmIdentifier algId;
    private final String publicFormat;
    private final Class<? extends EncodedKeySpec> publicKeySpecClass;
    private final String privateFormat;
    private final Class<? extends EncodedKeySpec> privateKeySpecClass;
    private final int keySizeMin;
    private final MimeType contentType;

    private KeyAlgorithm(String id, ASN1ObjectIdentifier oid, String publicFormat, Class<? extends EncodedKeySpec> publicKeySpecClass, String privateFormat,
        Class<? extends EncodedKeySpec> privateKeySpecClass, int keySizeMin, MimeType contentType) {
        this.id = id;
        this.oid = oid;
        this.algId = new AlgorithmIdentifier(this.oid);
        this.publicFormat = publicFormat;
        this.publicKeySpecClass = publicKeySpecClass;
        this.privateFormat = privateFormat;
        this.privateKeySpecClass = privateKeySpecClass;
        this.keySizeMin = keySizeMin;
        this.contentType = contentType;
    }

    @Override
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }

    public MimeType getContentType() {
        return this.contentType;
    }

    public String getFormat(KeyType keyType) {
        return (keyType == KeyType.PUBLIC) ? this.getPublicFormat() : this.getPrivateFormat();
    }

    public Class<? extends EncodedKeySpec> getKeySpecClass(KeyType keyType) {
        return (keyType == KeyType.PUBLIC) ? this.getPublicKeySpecClass() : this.getPrivateKeySpecClass();
    }

    @Override
    public String getId() {
        return this.id;
    }

    public int getKeySizeMin() {
        return this.keySizeMin;
    }

    @Override
    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }

    public String getPublicFormat() {
        return this.publicFormat;
    }

    public Class<? extends EncodedKeySpec> getPublicKeySpecClass() {
        return this.publicKeySpecClass;
    }

    public String getPrivateFormat() {
        return this.privateFormat;
    }

    public Class<? extends EncodedKeySpec> getPrivateKeySpecClass() {
        return this.privateKeySpecClass;
    }
}
