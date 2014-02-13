package gov.hhs.onc.dcdt.testcases.discovery.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential.DiscoveryTestcaseCredentialTypePredicate;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcase;
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
public class DiscoveryTestcaseImpl extends
    AbstractToolTestcase<DiscoveryTestcaseResultConfig, DiscoveryTestcaseResultInfo, DiscoveryTestcaseDescription, DiscoveryTestcaseResult> implements
    DiscoveryTestcase {
    private List<DiscoveryTestcaseCredential> creds;
    private MailAddress mailAddr;

    @Override
    @SuppressWarnings({ "ConstantConditions" })
    public void afterPropertiesSet() throws Exception {
        if (this.hasCredentials()) {
            CredentialConfig credConfig;
            CertificateConfig credCertConfig;
            CertificateName credCertConfigSubj;

            for (DiscoveryTestcaseCredential cred : this.creds) {
                if (cred.hasCredentialConfig() && (credConfig = cred.getCredentialConfig()).hasCertificateDescriptor()
                    && (credCertConfig = credConfig.getCertificateDescriptor()).hasSubject()
                    && !(credCertConfigSubj = credCertConfig.getSubject()).hasMailAddress()) {
                    credCertConfigSubj.setMailAddress(this.mailAddr);
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
        return CollectionUtils.select(this.creds, DiscoveryTestcaseCredentialTypePredicate.INSTANCE_TARGET);
    }

    @Override
    public boolean hasBackgroundCredentials() {
        return !this.getBackgroundCredentials().isEmpty();
    }

    @Override
    @Transient
    public Collection<DiscoveryTestcaseCredential> getBackgroundCredentials() {
        return CollectionUtils.select(this.creds, DiscoveryTestcaseCredentialTypePredicate.INSTANCE_BACKGROUND);
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
