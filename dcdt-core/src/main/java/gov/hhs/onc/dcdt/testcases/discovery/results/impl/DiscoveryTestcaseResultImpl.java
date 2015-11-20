package gov.hhs.onc.dcdt.testcases.discovery.results.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResult;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;
import org.bouncycastle.cms.SignerId;

public class DiscoveryTestcaseResultImpl extends AbstractToolTestcaseResult<DiscoveryTestcaseDescription, DiscoveryTestcase, DiscoveryTestcaseSubmission>
    implements DiscoveryTestcaseResult {
    private DiscoveryTestcaseCredential decryptCred;
    private DiscoveryTestcaseCredential expectedDecryptCred;
    private Map<SignerId, CertificateInfo> signerCertInfos;

    public DiscoveryTestcaseResultImpl(DiscoveryTestcaseSubmission submission, @Nullable List<CertificateDiscoveryStep> procSteps) {
        super(submission, procSteps);
    }

    @Override
    public boolean hasDecryptionCredential() {
        return (this.decryptCred != null);
    }

    @Nullable
    @Override
    public DiscoveryTestcaseCredential getDecryptionCredential() {
        return this.decryptCred;
    }

    @Override
    public void setDecryptionCredential(DiscoveryTestcaseCredential decryptCred) {
        this.decryptCred = decryptCred;
    }

    @Override
    public boolean hasExpectedDecryptionCredential() {
        return (this.expectedDecryptCred != null);
    }

    @Nullable
    @Override
    public DiscoveryTestcaseCredential getExpectedDecryptionCredential() {
        return this.expectedDecryptCred;
    }

    @Override
    public void setExpectedDecryptionCredential(DiscoveryTestcaseCredential expectedDecryptCred) {
        this.expectedDecryptCred = expectedDecryptCred;
    }

    @Override
    public boolean hasSignerCertificateInfos() {
        return !MapUtils.isEmpty(this.signerCertInfos);
    }

    @Nullable
    @Override
    public Map<SignerId, CertificateInfo> getSignerCertificateInfos() {
        return this.signerCertInfos;
    }

    @Override
    public void setSignerCertificateInfos(Map<SignerId, CertificateInfo> signerCertInfos) {
        this.signerCertInfos = signerCertInfos;
    }
}
