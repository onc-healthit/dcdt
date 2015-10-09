package gov.hhs.onc.dcdt.crypto.keys.impl;

import gov.hhs.onc.dcdt.crypto.keys.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.keys.KeyException;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.persistence.Transient;
import org.apache.commons.lang3.ObjectUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;

@Embeddable
public class KeyInfoImpl extends AbstractKeyDescriptor implements KeyInfo {
    private KeyPair keyPair;
    private AuthorityKeyIdentifier authKeyInfo;
    private PrivateKeyInfo privateKeyInfo;
    private SubjectPublicKeyInfo subjPublicKeyInfo;

    public KeyInfoImpl() throws KeyException {
        this(null);
    }

    public KeyInfoImpl(@Nullable PublicKey publicKey, @Nullable PrivateKey privateKey) throws KeyException {
        this(new KeyPair(publicKey, privateKey));
    }

    public KeyInfoImpl(@Nullable KeyPair keyPair) throws KeyException {
        this.setKeyPair(keyPair);
    }

    @Override
    protected void reset() {
        super.reset();

        this.authKeyInfo = null;
        this.privateKeyInfo = null;
        this.subjPublicKeyInfo = null;
    }

    private void processKeys() throws KeyException {
        this.reset();

        if (!this.hasAvailableKey()) {
            return;
        }

        try {
            Key availableKey = this.getAvailableKey();

            // noinspection ConstantConditions
            this.keyAlg = CryptographyUtils.findById(KeyAlgorithm.class, availableKey.getAlgorithm());
            this.keySize = ((RSAKey) availableKey).getModulus().bitLength();

            if (this.hasPublicKey()) {
                // noinspection ConstantConditions
                byte[] publicKeyData = this.getPublicKey().getEncoded();

                this.subjPublicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKeyData);

                try {
                    this.authKeyInfo = new JcaX509ExtensionUtils().createAuthorityKeyIdentifier(this.subjPublicKeyInfo);
                } catch (NoSuchAlgorithmException e) {
                    throw new KeyException(String.format("Unable to build key (algId=%s, algOid=%s, size=%s) authority key information.", this.keyAlg.getId(),
                        this.keyAlg.getOid(), this.keySize), e);
                }
            }

            if (this.hasPrivateKey()) {
                // noinspection ConstantConditions
                this.privateKeyInfo = PrivateKeyInfo.getInstance(this.getPrivateKey().getEncoded());
            }
        } catch (Exception e) {
            this.reset();

            throw e;
        }
    }

    @Override
    public boolean hasAuthorityKeyId() {
        return (this.authKeyInfo != null);
    }

    @Nullable
    @Override
    @Transient
    public AuthorityKeyIdentifier getAuthorityKeyId() {
        return this.authKeyInfo;
    }

    @Override
    public boolean hasAvailableKey() {
        return (this.getAvailableKey() != null);
    }

    @Nullable
    @Override
    @Transient
    public Key getAvailableKey() {
        return ObjectUtils.defaultIfNull(this.getPublicKey(), this.getPrivateKey());
    }

    @Override
    public boolean hasKeyPair() {
        return (this.keyPair != null);
    }

    @Nullable
    @Override
    @Transient
    public KeyPair getKeyPair() {
        return this.keyPair;
    }

    @Override
    public void setKeyPair(@Nullable KeyPair keyPair) throws KeyException {
        this.keyPair = keyPair;

        this.processKeys();
    }

    @Override
    public boolean hasPublicKey() {
        return (this.getPublicKey() != null);
    }

    @Nullable
    @Override
    @Transient
    public PublicKey getPublicKey() {
        return (this.hasKeyPair() ? this.keyPair.getPublic() : null);
    }

    @Override
    public void setPublicKey(@Nullable PublicKey publicKey) throws KeyException {
        this.setKeyPair(new KeyPair(publicKey, this.getPrivateKey()));
    }

    @Override
    public boolean hasPrivateKey() {
        return (this.getPrivateKey() != null);
    }

    @Column(name = "private_key_data", nullable = false)
    @Lob
    @Nullable
    @Override
    public PrivateKey getPrivateKey() {
        return (this.hasKeyPair() ? this.keyPair.getPrivate() : null);
    }

    @Override
    public void setPrivateKey(@Nullable PrivateKey privateKey) throws KeyException {
        this.setKeyPair(new KeyPair(this.getPublicKey(), privateKey));
    }

    @Override
    public boolean hasPrivateKeyInfo() {
        return (this.privateKeyInfo != null);
    }

    @Nullable
    @Override
    @Transient
    public PrivateKeyInfo getPrivateKeyInfo() {
        return this.privateKeyInfo;
    }

    @Override
    public boolean hasSubjectPublicKeyInfo() {
        return (this.subjPublicKeyInfo != null);
    }

    @Nullable
    @Override
    @Transient
    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.subjPublicKeyInfo;
    }
}
