package gov.hhs.onc.dcdt.testcases.discovery.results.impl;

import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResult;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class DiscoveryTestcaseResultImpl extends AbstractToolTestcaseResult implements DiscoveryTestcaseResult {
    private DiscoveryTestcaseCredential credFound;
    private DiscoveryTestcaseCredential credExpected;
    private String decryptionErrorMsg;

    @Override
    public DiscoveryTestcaseCredential getCredentialFound() {
        return this.credFound;
    }

    @Override
    public void setCredentialFound(DiscoveryTestcaseCredential credFound) {
        this.credFound = credFound;
    }

    @Override
    public DiscoveryTestcaseCredential getCredentialExpected() {
        return this.credExpected;
    }

    @Override
    public void setCredentialExpected(DiscoveryTestcaseCredential credExpected) {
        this.credExpected = credExpected;
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
}
