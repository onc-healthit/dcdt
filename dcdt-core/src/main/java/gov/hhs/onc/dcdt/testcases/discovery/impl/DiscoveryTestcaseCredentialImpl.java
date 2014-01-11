package gov.hhs.onc.dcdt.testcases.discovery.impl;


import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.crypto.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairInfo;
import gov.hhs.onc.dcdt.crypto.keys.impl.KeyPairInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.KeyUtils;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredential;
import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;

@Entity(name = "discovery_testcase_cred")
@Table(name = "discovery_testcase_creds")
public class DiscoveryTestcaseCredentialImpl extends AbstractToolBean implements DiscoveryTestcaseCredential {
    @Transient
    private CredentialConfig credConfig;

    @Transient
    private CredentialInfo credInfo;

    @Transient
    private DiscoveryTestcaseCredential issuerCred;

    @Override
    public boolean hasCertificateData() {
        try {
            return this.getCertificateData() != null;
        } catch (CryptographyException ignored) {
        }

        return false;
    }

    @Column(name = "cert_data", nullable = false)
    @Lob
    @Nullable
    @Override
    public byte[] getCertificateData() throws CryptographyException {
        return (this.hasCredentialInfo() && this.credInfo.hasCertificateDescriptor() && this.credInfo.getCertificateDescriptor().hasCertificate())
            ? CertificateUtils.writeCertificate(this.credInfo.getCertificateDescriptor().getCertificate(), DataEncoding.PEM) : null;
    }

    @Override
    public void setCertificateData(@Nullable byte[] certData) throws CryptographyException {
        if (this.credInfo == null) {
            this.credInfo = new CredentialInfoImpl();
        }

        CertificateInfo certInfo;

        if ((certInfo = this.credInfo.getCertificateDescriptor()) == null) {
            this.credInfo.setCertificateDescriptor(certInfo = new CertificateInfoImpl());
        }

        certInfo.setCertificate(CertificateUtils.readCertificate(certData, CertificateType.X509, DataEncoding.PEM));

        KeyPairInfo keyPairInfo;

        if ((keyPairInfo = this.credInfo.getKeyPairDescriptor()) == null) {
            this.credInfo.setKeyPairDescriptor(keyPairInfo = new KeyPairInfoImpl());
        }

        keyPairInfo.setPublicKey(certInfo.getCertificate().getPublicKey());
    }

    @Override
    public boolean hasCredentialConfig() {
        return this.credConfig != null;
    }

    @Nullable
    @Override
    @Transient
    public CredentialConfig getCredentialConfig() {
        return this.credConfig;
    }

    @Override
    public void setCredentialConfig(@Nullable CredentialConfig credConfig) {
        this.credConfig = credConfig;
    }

    @Override
    public boolean hasCredentialInfo() {
        return this.credInfo != null;
    }

    @Nullable
    @Override
    @Transient
    public CredentialInfo getCredentialInfo() {
        return this.credInfo;
    }

    @Override
    public void setCredentialInfo(@Nullable CredentialInfo credInfo) {
        this.credInfo = credInfo;
    }

    @Override
    public boolean hasIssuerCredential() {
        return this.issuerCred != null;
    }

    @JoinColumn(name = "issuer_name", referencedColumnName = "name")
    @ManyToOne(targetEntity = DiscoveryTestcaseCredentialImpl.class, cascade = CascadeType.ALL)
    @Nullable
    @Override
    public DiscoveryTestcaseCredential getIssuerCredential() {
        return this.issuerCred;
    }

    @Override
    public void setIssuerCredential(@Nullable DiscoveryTestcaseCredential issuerCred) {
        this.issuerCred = issuerCred;
    }

    @Override
    public boolean hasName() {
        return !StringUtils.isBlank(this.getName());
    }

    @Column(name = "name", updatable = false, nullable = false)
    @Id
    @Nullable
    @Override
    public String getName() {
        return (this.hasCredentialConfig() && this.credConfig.hasCertificateDescriptor() && this.credConfig.getCertificateDescriptor().hasSubject())
            ? this.credConfig.getCertificateDescriptor().getSubject().toString() : null;
    }

    @Override
    public void setName(@Nullable String name) {
    }

    @Override
    public boolean hasPrivateKeyData() {
        try {
            return this.getPrivateKeyData() != null;
        } catch (CryptographyException ignored) {
        }

        return false;
    }

    @Column(name = "private_key_data", nullable = false)
    @Lob
    @Nullable
    @Override
    public byte[] getPrivateKeyData() throws CryptographyException {
        return (this.hasCredentialInfo() && this.credInfo.hasKeyPairDescriptor() && this.credInfo.getKeyPairDescriptor().hasPrivateKey()) ? KeyUtils
            .writePrivateKey(this.credInfo.getKeyPairDescriptor().getPrivateKey(), DataEncoding.PEM) : null;
    }

    @Override
    public void setPrivateKeyData(@Nullable byte[] privateKeyData) throws CryptographyException {
        if (this.credInfo == null) {
            this.credInfo = new CredentialInfoImpl();
        }

        KeyPairInfo keyPairInfo;

        if ((keyPairInfo = this.credInfo.getKeyPairDescriptor()) == null) {
            this.credInfo.setKeyPairDescriptor(keyPairInfo = new KeyPairInfoImpl());
        }

        keyPairInfo.setPrivateKey(KeyUtils.readPrivateKey(privateKeyData, KeyAlgorithm.RSA, DataEncoding.PEM));
    }
}
