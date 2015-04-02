package gov.hhs.onc.dcdt.testcases.discovery.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialLocation;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialType;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcase;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.collections4.CollectionUtils;

@Entity(name = "discovery_testcase")
@JsonTypeName("discoveryTestcase")
@Table(name = "discovery_testcases")
public class DiscoveryTestcaseImpl extends AbstractToolTestcase<DiscoveryTestcaseDescription> implements DiscoveryTestcase {
    private List<DiscoveryTestcaseCredential> creds;
    private MailAddress mailAddr;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.hasCredentials() && this.hasMailAddress()) {
            BindingType credBindingType;
            MailAddress credMailAddrBound;
            CredentialConfig credConfig;
            CertificateConfig credCertConfig;
            CertificateName credCertConfigSubj;
            DiscoveryTestcaseCredentialLocation credLoc;

            for (DiscoveryTestcaseCredential cred : this.creds) {
                // noinspection ConstantConditions
                if (cred.hasBindingType() && (credBindingType = cred.getBindingType()).isBound()
                    && ((credMailAddrBound = this.mailAddr.forBindingType(credBindingType)) != null)) {
                    // noinspection ConstantConditions
                    if (cred.hasCredentialConfig() && (credConfig = cred.getCredentialConfig()).hasCertificateDescriptor()
                        && (credCertConfig = credConfig.getCertificateDescriptor()).hasSubjectName()
                        && !(credCertConfigSubj = credCertConfig.getSubjectName()).hasMailAddress()) {
                        credCertConfigSubj.setMailAddress(credMailAddrBound);
                    }

                    // noinspection ConstantConditions
                    if (cred.hasLocation() && !(credLoc = cred.getLocation()).hasMailAddress()) {
                        credLoc.setMailAddress(credMailAddrBound);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasTargetCredentials() {
        return !this.getTargetCredentials().isEmpty();
    }

    @Override
    @Transient
    public Collection<DiscoveryTestcaseCredential> getTargetCredentials() {
        return ToolStreamUtils.filter(this.creds, cred -> cred.getType() == DiscoveryTestcaseCredentialType.TARGET);
    }

    @Override
    public boolean hasBackgroundCredentials() {
        return !this.getBackgroundCredentials().isEmpty();
    }

    @Override
    @Transient
    public Collection<DiscoveryTestcaseCredential> getBackgroundCredentials() {
        return ToolStreamUtils.filter(this.creds, cred -> cred.getType() == DiscoveryTestcaseCredentialType.BACKGROUND);
    }

    @Override
    public boolean hasCredentials() {
        return !CollectionUtils.isEmpty(this.creds);
    }

    @Nullable
    @Override
    @Transient
    public List<DiscoveryTestcaseCredential> getCredentials() {
        return this.creds;
    }

    @Override
    public void setCredentials(List<DiscoveryTestcaseCredential> creds) {
        this.creds = creds;
    }

    @Override
    public boolean hasMailAddress() {
        return this.mailAddr != null;
    }

    @Nullable
    @Override
    @Transient
    public MailAddress getMailAddress() {
        return this.mailAddr;
    }

    @Override
    public void setMailAddress(@Nullable MailAddress mailAddr) {
        this.mailAddr = mailAddr;
    }
}
