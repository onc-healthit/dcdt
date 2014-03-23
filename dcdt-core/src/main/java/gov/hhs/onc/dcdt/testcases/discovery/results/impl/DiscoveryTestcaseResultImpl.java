package gov.hhs.onc.dcdt.testcases.discovery.results.impl;

import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultCredentialType;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResult;
import javax.annotation.Nullable;

public class DiscoveryTestcaseResultImpl extends AbstractToolTestcaseResult implements DiscoveryTestcaseResult {
    private DiscoveryTestcaseCredential credExpected;
    private DiscoveryTestcaseCredential credFound;
    private MailInfo mailInfo;
    private DiscoveryTestcase testcase;

    @Override
    public boolean hasCredential(DiscoveryTestcaseResultCredentialType credType) {
        return (this.getCredential(credType) != null);
    }

    @Nullable
    @Override
    public DiscoveryTestcaseCredential getCredential(DiscoveryTestcaseResultCredentialType credType) {
        return ((credType == DiscoveryTestcaseResultCredentialType.EXPECTED) ? this.credExpected : this.credFound);
    }

    @Override
    public boolean hasCredentialExpected() {
        return this.credExpected != null;
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
        return this.credFound != null;
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
    public boolean hasMailInfo() {
        return this.mailInfo != null;
    }

    @Nullable
    @Override
    public MailInfo getMailInfo() {
        return this.mailInfo;
    }

    @Override
    public void setMailInfo(@Nullable MailInfo mailInfo) {
        this.mailInfo = mailInfo;
    }

    @Override
    public boolean hasTestcase() {
        return this.testcase != null;
    }

    @Nullable
    @Override
    public DiscoveryTestcase getTestcase() {
        return this.testcase;
    }

    @Override
    public void setTestcase(@Nullable DiscoveryTestcase testcase) {
        this.testcase = testcase;
    }
}
