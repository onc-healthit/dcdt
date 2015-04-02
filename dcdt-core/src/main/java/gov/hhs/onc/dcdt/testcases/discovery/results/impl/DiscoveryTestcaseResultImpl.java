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
import javax.annotation.Nullable;

public class DiscoveryTestcaseResultImpl extends AbstractToolTestcaseResult<DiscoveryTestcaseDescription, DiscoveryTestcase, DiscoveryTestcaseSubmission>
    implements DiscoveryTestcaseResult {
    private DiscoveryTestcaseCredential decryptCred;
    private DiscoveryTestcaseCredential expectedDecryptCred;
    private CertificateInfo signerCertInfo;

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
    public boolean hasSignerCertificateInfo() {
        return (this.signerCertInfo != null);
    }

    @Nullable
    @Override
    public CertificateInfo getSignerCertificateInfo() {
        return this.signerCertInfo;
    }

    @Override
    public void setSignerCertificateInfo(CertificateInfo signerCertInfo) {
        this.signerCertInfo = signerCertInfo;
    }
}
