package gov.hhs.onc.dcdt.testcases.discovery.results;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.impl.DiscoveryTestcaseResultInfoImpl;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(DiscoveryTestcaseResultInfoImpl.class) })
public interface DiscoveryTestcaseResultInfo extends ToolTestcaseResultInfo {
    public boolean isSuccessful();

    public void setSuccessful(boolean successful);

    public DiscoveryTestcaseCredential getCredentialFound();

    public void setCredentialFound(DiscoveryTestcaseCredential credFound);

    public DiscoveryTestcaseCredential getCredentialExpected();

    public void setCredentialExpected(DiscoveryTestcaseCredential credExpected);

    public boolean hasDecryptionErrorMessage();

    @Nullable
    public String getDecryptionErrorMessage();

    public void setDecryptionErrorMessage(@Nullable String decryptionErrorMsg);
}
