package gov.hhs.onc.dcdt.testcases.discovery.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredential.DiscoveryTestcaseCredentialTypePredicate;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcase;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity(name = "discovery_testcase")
@JsonTypeName("discoveryTestcase")
@Table(name = "discovery_testcases")
public class DiscoveryTestcaseImpl extends AbstractToolTestcase<DiscoveryTestcaseDescription, DiscoveryTestcaseResult> implements DiscoveryTestcase {
    private List<DiscoveryTestcaseCredential> creds;
    private MailAddress mailAddr;

    @Override
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

    @Cascade({ CascadeType.ALL })
    @JoinColumns({ @JoinColumn(name = "discovery_testcase_name", referencedColumnName = "name", nullable = false) })
    @Nullable
    @OneToMany(targetEntity = DiscoveryTestcaseCredentialImpl.class, orphanRemoval = true)
    @OrderBy("name")
    @Override
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
