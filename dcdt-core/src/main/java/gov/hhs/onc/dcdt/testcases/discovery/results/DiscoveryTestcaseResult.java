package gov.hhs.onc.dcdt.testcases.discovery.results;

import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
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

    public boolean hasMailInfo();

    @Nullable
    public MailInfo getMailInfo();

    public void setMailInfo(@Nullable MailInfo mailInfo);

    public boolean hasTestcase();

    @Nullable
    public DiscoveryTestcase getTestcase();

    public void setTestcase(@Nullable DiscoveryTestcase testcase);
}
