package gov.hhs.onc.dcdt.testcases.discovery.credentials.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.credentials.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.crl.CrlConfig;
import gov.hhs.onc.dcdt.crypto.crl.CrlEntryConfig;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.net.utils.ToolUriUtils;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialDescription;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialLocation;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialType;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.annotation.Nullable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Target;
import org.springframework.core.Ordered;

@Entity(name = "discovery_testcase_cred")
@JsonTypeName("discoveryTestcaseCred")
@Table(name = "discovery_testcase_creds")
public class DiscoveryTestcaseCredentialImpl extends AbstractToolNamedBean implements DiscoveryTestcaseCredential {
    private BindingType bindingType;
    private CredentialConfig credConfig;
    private CredentialInfo credInfo;
    private CrlConfig crlConfig;
    private CrlEntryConfig crlEntryConfig;
    private DiscoveryTestcaseCredentialDescription desc;
    private DiscoveryTestcaseCredential issuerCred;
    private DiscoveryTestcaseCredentialLocation loc;
    private int order = Ordered.LOWEST_PRECEDENCE;
    private DiscoveryTestcaseCredentialType type;
    private boolean valid = true;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!this.hasCredentialInfo() && this.hasIssuerCredential() && this.issuerCred.hasName() && this.hasCredentialConfig()
            && this.credConfig.hasCertificateDescriptor()) {
            CertificateConfig credCertConfig = this.credConfig.getCertificateDescriptor();
            String issuerCredName = this.issuerCred.getName();

            // noinspection ConstantConditions
            if (this.issuerCred.hasCrlConfig() && credCertConfig.hasCrlDistributionUris()) {
                // noinspection ConstantConditions
                String credCertConfigCrlDistribUriPath =
                    (ToolUriUtils.PATH_DELIM + issuerCredName + FilenameUtils.EXTENSION_SEPARATOR + this.issuerCred.getCrlConfig().getCrlType()
                        .getFileNameExtension());
                URI[] credCertConfigCrlDistribUris = ToolCollectionUtils.toArray(credCertConfig.getCrlDistributionUris(), URI.class);
                URI credCertConfigCrlDistribUri;

                // noinspection ConstantConditions
                for (int a = 0; a < credCertConfigCrlDistribUris.length; a++) {
                    if (!StringUtils.isEmpty((credCertConfigCrlDistribUri = credCertConfigCrlDistribUris[a]).getPath())) {
                        continue;
                    }

                    try {
                        credCertConfigCrlDistribUris[a] =
                            new URI(credCertConfigCrlDistribUri.getScheme(), null, credCertConfigCrlDistribUri.getHost(),
                                credCertConfigCrlDistribUri.getPort(), credCertConfigCrlDistribUriPath, null, null);
                    } catch (URISyntaxException e) {
                        throw new ToolBeanRegistryException(String.format("Unable to build Discovery testcase credential (name=%s) CRL distribution URI.",
                            this.name), e);
                    }
                }

                credCertConfig.setCrlDistributionUris(ToolArrayUtils.asSet(credCertConfigCrlDistribUris));
            }

            // noinspection ConstantConditions
            if (credCertConfig.hasIssuerAccessUris()) {
                // noinspection ConstantConditions
                String credCertConfigIssuerAccessUriPath =
                    (ToolUriUtils.PATH_DELIM + issuerCredName + FilenameUtils.EXTENSION_SEPARATOR + this.issuerCred.getCredentialConfig()
                        .getCertificateDescriptor().getCertificateType().getFileNameExtension());

                URI[] credCertConfigIssuerAccessUris = ToolCollectionUtils.toArray(credCertConfig.getIssuerAccessUris(), URI.class);
                URI credCertConfigIssuerAccessUri;

                // noinspection ConstantConditions
                for (int a = 0; a < credCertConfigIssuerAccessUris.length; a++) {
                    if (!StringUtils.isEmpty((credCertConfigIssuerAccessUri = credCertConfigIssuerAccessUris[a]).getPath())) {
                        continue;
                    }

                    try {
                        credCertConfigIssuerAccessUris[a] =
                            new URI(credCertConfigIssuerAccessUri.getScheme(), null, credCertConfigIssuerAccessUri.getHost(),
                                credCertConfigIssuerAccessUri.getPort(), credCertConfigIssuerAccessUriPath, null, null);
                    } catch (URISyntaxException e) {
                        throw new ToolBeanRegistryException(String.format("Unable to build Discovery testcase credential (name=%s) issuer access URI.",
                            this.name), e);
                    }
                }

                credCertConfig.setIssuerAccessUris(ToolArrayUtils.asSet(credCertConfigIssuerAccessUris));
            }
        }

        CertificateInfo credCertInfo;

        // noinspection ConstantConditions
        if (this.hasCredentialInfo() && this.credInfo.hasCertificateDescriptor() && (credCertInfo = this.credInfo.getCertificateDescriptor()).hasCertificate()) {
            KeyInfo credKeyInfo;

            // noinspection ConstantConditions
            if (this.credInfo.hasKeyDescriptor() && !(credKeyInfo = this.credInfo.getKeyDescriptor()).hasPublicKey()) {
                // noinspection ConstantConditions
                credKeyInfo.setPublicKey(credCertInfo.getCertificate().getPublicKey());
            }

            if (this.hasCrlConfig() && !this.crlConfig.hasIssuerDn()) {
                this.crlConfig.setIssuerDn(credCertInfo.getSubjectDn());
            }

            if (this.hasCrlEntryConfig()) {
                // noinspection ConstantConditions
                BigInteger credCertSerialNum = credCertInfo.getSerialNumber().getValue();

                if (!this.crlEntryConfig.hasSerialNumber()) {
                    this.crlEntryConfig.setSerialNumber(credCertSerialNum);
                }

                if (!this.crlEntryConfig.hasRevocationDate()) {
                    // noinspection ConstantConditions
                    this.crlEntryConfig.setRevocationDate(credCertInfo.getInterval().getNotAfter());
                }

                Map<BigInteger, CrlEntryConfig> issuerCredCrlConfigEntries;

                // noinspection ConstantConditions
                if (this.hasIssuerCredential() && this.issuerCred.hasCrlConfig()
                    && !(issuerCredCrlConfigEntries = this.issuerCred.getCrlConfig().getEntries()).containsKey(credCertSerialNum)) {
                    issuerCredCrlConfigEntries.put(credCertSerialNum, this.crlEntryConfig);
                }
            }
        }
    }

    @Override
    public boolean hasBindingType() {
        return this.bindingType != null;
    }

    @Nullable
    @Override
    @Transient
    public BindingType getBindingType() {
        return this.bindingType;
    }

    @Override
    public void setBindingType(@Nullable BindingType bindingType) {
        this.bindingType = bindingType;
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

    @AttributeOverrides({ @AttributeOverride(name = "keyDescriptor.privateKey", column = @Column(name = "private_key_data", nullable = false)),
        @AttributeOverride(name = "certificateDescriptor.certificate", column = @Column(name = "cert_data", nullable = false)) })
    @Embedded
    @Nullable
    @Override
    @Target(CredentialInfoImpl.class)
    public CredentialInfo getCredentialInfo() {
        return this.credInfo;
    }

    @Override
    public void setCredentialInfo(@Nullable CredentialInfo credInfo) {
        this.credInfo = credInfo;
    }

    @Override
    public boolean hasCrlConfig() {
        return (this.crlConfig != null);
    }

    @Nullable
    @Override
    @Transient
    public CrlConfig getCrlConfig() {
        return this.crlConfig;
    }

    @Override
    public void setCrlConfig(@Nullable CrlConfig crlConfig) {
        this.crlConfig = crlConfig;
    }

    @Override
    public boolean hasCrlEntryConfig() {
        return (this.crlEntryConfig != null);
    }

    @Nullable
    @Override
    @Transient
    public CrlEntryConfig getCrlEntryConfig() {
        return this.crlEntryConfig;
    }

    @Override
    public void setCrlEntryConfig(@Nullable CrlEntryConfig crlEntryConfig) {
        this.crlEntryConfig = crlEntryConfig;
    }

    @Override
    public boolean hasDescription() {
        return this.desc != null;
    }

    @Nullable
    @Override
    @Transient
    public DiscoveryTestcaseCredentialDescription getDescription() {
        return this.desc;
    }

    @Override
    public void setDescription(@Nullable DiscoveryTestcaseCredentialDescription desc) {
        this.desc = desc;
    }

    @Override
    public boolean hasIssuerCredential() {
        return this.issuerCred != null;
    }

    @Nullable
    @Override
    @Transient
    public DiscoveryTestcaseCredential getIssuerCredential() {
        return this.issuerCred;
    }

    @Override
    public void setIssuerCredential(@Nullable DiscoveryTestcaseCredential issuerCred) {
        this.issuerCred = issuerCred;
    }

    @Override
    public boolean hasLocation() {
        return this.loc != null;
    }

    @Nullable
    @Override
    @Transient
    public DiscoveryTestcaseCredentialLocation getLocation() {
        return this.loc;
    }

    @Override
    public void setLocation(DiscoveryTestcaseCredentialLocation loc) {
        this.loc = loc;
    }

    @Column(name = "name", nullable = false)
    @Id
    @Nullable
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    @Transient
    public int getOrder() {
        return this.order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean hasType() {
        return this.type != null;
    }

    @Nullable
    @Override
    @Transient
    public DiscoveryTestcaseCredentialType getType() {
        return this.type;
    }

    @Override
    public void setType(@Nullable DiscoveryTestcaseCredentialType type) {
        this.type = type;
    }

    @Override
    @Transient
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
