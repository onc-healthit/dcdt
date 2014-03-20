package gov.hhs.onc.dcdt.testcases.discovery.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResultDescriptor;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

@JsonTypeName("discoveryTestcaseResultInfo")
public class DiscoveryTestcaseResultInfoImpl extends AbstractToolTestcaseResultDescriptor implements DiscoveryTestcaseResultInfo {
    private DiscoveryTestcaseCredential credExpected;
    private DiscoveryTestcaseCredential credFound;
    private String decryptionErrorMsg;
    private boolean successful;

    @Override
    public boolean hasCredentialExcepted() {
        return (this.credExpected != null);
    }

    @Nullable
    @Override
    public DiscoveryTestcaseCredential getCredentialExpected() {
        return this.credExpected;
    }

    @Override
    public void setCredentialExpected(@Nullable DiscoveryTestcaseCredential credExpected) {
        this.credExpected = credExpected;
    }

    @Override
    public boolean hasCredentialFound() {
        return (this.credFound != null);
    }

    @Nullable
    @Override
    public DiscoveryTestcaseCredential getCredentialFound() {
        return this.credFound;
    }

    @Override
    public void setCredentialFound(@Nullable DiscoveryTestcaseCredential credFound) {
        this.credFound = credFound;
    }

    @Override
    public boolean hasDecryptionErrorMessage() {
        return !StringUtils.isBlank(this.decryptionErrorMsg);
    }

    @Nullable
    @Override
    public String getDecryptionErrorMessage() {
        return this.decryptionErrorMsg;
    }

    @Override
    public void setDecryptionErrorMessage(@Nullable String decryptionErrorMsg) {
        this.decryptionErrorMsg = decryptionErrorMsg;
    }

    @Override
    public boolean isSuccessful() {
        return this.successful;
    }

    @Override
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
