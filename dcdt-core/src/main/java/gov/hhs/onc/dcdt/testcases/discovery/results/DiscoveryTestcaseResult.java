package gov.hhs.onc.dcdt.testcases.discovery.results;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseResult extends ToolTestcaseResult<DiscoveryTestcaseDescription, DiscoveryTestcase, DiscoveryTestcaseSubmission> {
    public boolean hasDecryptionCredential();

    @Nullable
    public DiscoveryTestcaseCredential getDecryptionCredential();

    public void setDecryptionCredential(DiscoveryTestcaseCredential decryptCred);

    public boolean hasExpectedDecryptionCredential();

    @Nullable
    public DiscoveryTestcaseCredential getExpectedDecryptionCredential();

    public void setExpectedDecryptionCredential(DiscoveryTestcaseCredential expectedDecryptCred);

    public boolean hasSignerCertificateInfo();

    @Nullable
    public CertificateInfo getSignerCertificateInfo();

    public void setSignerCertificateInfo(CertificateInfo signerCertInfo);
}
