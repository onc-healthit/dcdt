package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyInfo;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.annotation.Nullable;
import javax.persistence.Transient;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public interface KeyInfo extends KeyDescriptor, CryptographyInfo {
   
    public boolean hasIssuerPublicKey();

    @Nullable
    public PublicKey getIssuerPublicKey();


    public void setIssuerPublicKey(@Nullable PublicKey issuerPublicKey) throws KeyException;

    public boolean hasSubjectKeyIdentifier();

    @Nullable
    public SubjectKeyIdentifier getSubjectKeyId();

    public boolean hasAuthorityKeyId();

    @Nullable
    public AuthorityKeyIdentifier getAuthorityKeyId();

    public boolean hasAvailableKey();

    @Nullable
    public Key getAvailableKey();

    public boolean hasKeyPair();

    @Nullable
    public KeyPair getKeyPair();

    public void setKeyPair(@Nullable KeyPair keyPair) throws KeyException;

    public boolean hasPublicKey();

    @Nullable
    public PublicKey getPublicKey();

    public void setPublicKey(@Nullable PublicKey publicKey) throws KeyException;

    public boolean hasPrivateKey();

    @Nullable
    public PrivateKey getPrivateKey();

    public void setPrivateKey(@Nullable PrivateKey privateKey) throws KeyException;

    public boolean hasPrivateKeyInfo();

    @Nullable
    public PrivateKeyInfo getPrivateKeyInfo();

    public boolean hasSubjectPublicKeyInfo();

    @Nullable
    public SubjectPublicKeyInfo getSubjectPublicKeyInfo();
}
