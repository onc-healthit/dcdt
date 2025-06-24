package gov.hhs.onc.dcdt.crypto.keys.impl;

import gov.hhs.onc.dcdt.crypto.keys.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.keys.KeyException;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.impl.DiscoveryTestcaseCredentialRegistryImpl;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.persistence.Transient;
import org.apache.commons.lang3.ObjectUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Embeddable
public class KeyInfoImpl extends AbstractKeyDescriptor implements KeyInfo {
    private KeyPair keyPair;
    private AuthorityKeyIdentifier authKeyInfo;
    private SubjectKeyIdentifier subjKeyInfo;
    private PrivateKeyInfo privateKeyInfo;
    private SubjectPublicKeyInfo subjPublicKeyInfo;
    private PublicKey issuerPublicKey;
    private final static Logger LOGGER = LoggerFactory.getLogger(KeyInfoImpl.class);

    public KeyInfoImpl() throws KeyException {
        this(null);
    }

    public KeyInfoImpl(@Nullable PublicKey publicKey, @Nullable PrivateKey privateKey) throws KeyException {
        this(new KeyPair(publicKey, privateKey));
    }

    public KeyInfoImpl(@Nullable KeyPair keyPair) throws KeyException {
        this.setKeyPair(keyPair);
    }

    public KeyInfoImpl(@Nullable KeyPair keyPair, @Nullable PublicKey issuerPublicKey) throws KeyException {
        this.issuerPublicKey = issuerPublicKey; // Set issuerPublicKey first
        this.setKeyPair(keyPair); // Process keys
    }


    @Override
    protected void reset() {
        super.reset();

        this.authKeyInfo = null;
        this.privateKeyInfo = null;
        this.subjPublicKeyInfo = null;
        this.subjKeyInfo = null;
    }


    private static String printSubjectPublicKeyInfo(SubjectPublicKeyInfo subjPublicKeyInfo) {
        String readableInfo = "";
        if (subjPublicKeyInfo != null) {
            // Convert the SubjectPublicKeyInfo to a readable format using ASN1Dump
            readableInfo = ASN1Dump.dumpAsString(subjPublicKeyInfo, true);

        }
        return readableInfo;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexBuilder.append(String.format("%02X", b));
        }
        return hexBuilder.toString();
    }


    private void processKeys() throws KeyException {
        this.reset();

        if (!this.hasAvailableKey()) {
            return;
        }

        try {
            Key availableKey = this.getAvailableKey();

            // noinspection ConstantConditions
            this.keyAlg = ToolEnumUtils.findById(KeyAlgorithm.class, availableKey.getAlgorithm());
            this.keySize = ((RSAKey) availableKey).getModulus().bitLength();

            // Process Issuer Public Key if present
            if (this.hasIssuerPublicKey()) {
                PublicKey issuerPublicKey = this.getIssuerPublicKey();
                LOGGER.info("Issuer Public Key from KeyInfoImpl: {} ", this.issuerPublicKey);
                // Convert to SubjectPublicKeyInfo
                byte[] issuerPublicKeyData = issuerPublicKey.getEncoded();
                SubjectPublicKeyInfo issuerSubjPublicKeyInfo = SubjectPublicKeyInfo.getInstance(issuerPublicKeyData);

                // Create AuthorityKeyIdentifier from Issuer Public Key
                try {
                    this.authKeyInfo = new JcaX509ExtensionUtils().createAuthorityKeyIdentifier(issuerSubjPublicKeyInfo);
                    LOGGER.info("Authority Key Identifier (Issuer) from KeyInfoImpl: {} ", bytesToHex(this.authKeyInfo.getKeyIdentifier()));
                } catch (NoSuchAlgorithmException e) {
                    throw new KeyException("Unable to create Authority Key Identifier from Issuer Public Key.", e);
                }
            }

            if (this.hasPublicKey()) {
                PublicKey publicKey = this.getPublicKey();
                LOGGER.debug("Public Key: {}", publicKey);
                // noinspection ConstantConditions
                byte[] publicKeyData = publicKey.getEncoded();

                this.subjPublicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKeyData);
                LOGGER.debug("SubjectPublicKeyInfo: {}", printSubjectPublicKeyInfo(this.subjPublicKeyInfo));

                    try {
                        this.subjKeyInfo= new JcaX509ExtensionUtils().createSubjectKeyIdentifier(subjPublicKeyInfo);
                        LOGGER.info("Subject Key Identifier generated from KeyInfoImpl: {} ", bytesToHex(this.subjKeyInfo.getKeyIdentifier()));

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
    public boolean hasIssuerPublicKey() {
        return (this.issuerPublicKey != null);
    }


    @Nullable
    @Override
    @Transient
    public PublicKey getIssuerPublicKey() {
        return this.issuerPublicKey;
    }

    @Override
    public void setIssuerPublicKey(@Nullable PublicKey issuerPublicKey) throws KeyException {
        this.issuerPublicKey = issuerPublicKey;
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

    @Override
    public boolean hasSubjectKeyIdentifier() {
        return (this.subjKeyInfo != null);
    }

    @Nullable
    @Transient
    @Override
    public SubjectKeyIdentifier getSubjectKeyId() {
        return this.subjKeyInfo;
    }
}
