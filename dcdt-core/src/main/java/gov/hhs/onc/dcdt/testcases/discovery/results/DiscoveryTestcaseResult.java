package gov.hhs.onc.dcdt.testcases.discovery.results;

import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseResult extends ToolTestcaseResult {
    public boolean hasCredentialExpected();

    @Nullable
    public DiscoveryTestcaseCredential getCredentialExpected();

    public void setCredentialExpected(@Nullable DiscoveryTestcaseCredential credExpected);

    public boolean hasCredentialFound();

    @Nullable
    public DiscoveryTestcaseCredential getCredentialFound();

    public void setCredentialFound(@Nullable DiscoveryTestcaseCredential credFound);

    public boolean hasDecryptionErrorMessage();

    @Nullable
    public String getDecryptionErrorMessage();

    public void setDecryptionErrorMessage(@Nullable String decryptionErrorMsg);
}
