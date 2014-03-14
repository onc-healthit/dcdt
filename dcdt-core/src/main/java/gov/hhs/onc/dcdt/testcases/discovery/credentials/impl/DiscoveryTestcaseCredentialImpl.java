package gov.hhs.onc.dcdt.testcases.discovery.credentials.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.credentials.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialDescription;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialLocation;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialType;
import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import javax.annotation.Nullable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Target;
import org.springframework.context.MessageSource;

@Entity(name = "discovery_testcase_cred")
@JsonTypeName("discoveryTestcaseCred")
@Table(name = "discovery_testcase_creds")
public class DiscoveryTestcaseCredentialImpl extends AbstractToolNamedBean implements DiscoveryTestcaseCredential {
    private BindingType bindingType;
    private CredentialConfig credConfig;
    private CredentialInfo credInfo;
    private DiscoveryTestcaseCredentialDescription desc;
    private DiscoveryTestcaseCredential issuerCred;
    private DiscoveryTestcaseCredentialLocation loc;
    private DiscoveryTestcaseCredentialType type;
    private boolean valid = true;
    private MessageSource msgSource;

    @Override
    public void afterPropertiesSet() throws Exception {
        KeyInfo credKeyInfo;
        CertificateInfo credCertInfo;

        // noinspection ConstantConditions
        if (this.hasCredentialInfo() && this.credInfo.hasKeyDescriptor() && !(credKeyInfo = this.credInfo.getKeyDescriptor()).hasPublicKey()
            && this.credInfo.hasCertificateDescriptor() && (credCertInfo = this.credInfo.getCertificateDescriptor()).hasCertificate()) {
            // noinspection ConstantConditions
            credKeyInfo.setPublicKey(credCertInfo.getCertificate().getPublicKey());
        }
    }

    @Override
    public String toString() {
        ToolStrBuilder credStrBuilder = new ToolStrBuilder();

        credStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.cred.name.msg", new Object[] { this.name }),
            StringUtils.LF);
        credStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.cred.valid.msg", this.valid), StringUtils.LF);
        credStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.cred.bindingType.msg", this.bindingType),
            StringUtils.LF);
        credStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.cred.location.msg"), StringUtils.LF);
        credStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.cred.locationType.msg", this.loc.getType()),
            StringUtils.LF);
        // noinspection ConstantConditions
        credStrBuilder.appendWithDelimiter(
            ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.cred.mailAddr.msg", new Object[] { this.loc.getMailAddress().toAddress() }),
            StringUtils.LF);

        if (this.loc.hasLdapConfig()) {
            InstanceLdapConfig ldapConfig = this.loc.getLdapConfig();
            // noinspection ConstantConditions
            credStrBuilder.appendWithDelimiter(
                ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.cred.host.msg", new Object[] { ldapConfig.getHost().toString() }),
                StringUtils.LF);
            credStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.cred.port.msg", ldapConfig.getPort()),
                StringUtils.LF);
        }

        credStrBuilder.appendWithDelimiter(
            ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.cred.desc.msg", new Object[] { this.desc.getText() }), StringUtils.LF);

        return credStrBuilder.build();
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

    @Cascade({ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE, CascadeType.REPLICATE, CascadeType.LOCK,
        CascadeType.DETACH })
    @JoinColumns({ @JoinColumn(name = "issuer_name", referencedColumnName = "name") })
    @ManyToOne(targetEntity = DiscoveryTestcaseCredentialImpl.class, optional = false)
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

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.msgSource = messageSource;
    }
}
