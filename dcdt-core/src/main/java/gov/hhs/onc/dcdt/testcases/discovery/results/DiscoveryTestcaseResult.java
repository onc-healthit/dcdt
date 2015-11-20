package gov.hhs.onc.dcdt.testcases.discovery.results;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import java.util.Map;
import javax.annotation.Nullable;
import org.bouncycastle.cms.SignerId;

public interface DiscoveryTestcaseResult extends ToolTestcaseResult<DiscoveryTestcaseDescription, DiscoveryTestcase, DiscoveryTestcaseSubmission> {
    public boolean hasDecryptionCredential();

    @Nullable
    public DiscoveryTestcaseCredential getDecryptionCredential();

    public void setDecryptionCredential(DiscoveryTestcaseCredential decryptCred);

    public boolean hasExpectedDecryptionCredential();

    @Nullable
    public DiscoveryTestcaseCredential getExpectedDecryptionCredential();

    public void setExpectedDecryptionCredential(DiscoveryTestcaseCredential expectedDecryptCred);

    public boolean hasSignerCertificateInfos();

    @Nullable
    public Map<SignerId, CertificateInfo> getSignerCertificateInfos();

    public void setSignerCertificateInfos(Map<SignerId, CertificateInfo> signerCertInfos);
}
