package gov.hhs.onc.dcdt.testcases.discovery.results;

import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseResult extends ToolTestcaseResult {
    public DiscoveryTestcaseCredential getCredentialFound();

    public void setCredentialFound(DiscoveryTestcaseCredential credFound);

    public DiscoveryTestcaseCredential getCredentialExpected();

    public void setCredentialExpected(DiscoveryTestcaseCredential credExpected);

    public boolean hasDecryptionErrorMessage();

    @Nullable
    public String getDecryptionErrorMessage();

    public void setDecryptionErrorMessage(@Nullable String decryptionErrorMsg);
}
