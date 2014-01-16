package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

public interface DiscoveryTestcase extends ToolTestcase<DiscoveryTestcaseDescription, DiscoveryTestcaseResult> {
    public boolean hasTargetCredential();

    @Nullable
    public DiscoveryTestcaseCredential getTargetCredential();

    public boolean hasBackgroundCredentials();

    public Collection<DiscoveryTestcaseCredential> getBackgroundCredentials();

    public boolean hasCredentials();

    @Nullable
    public List<DiscoveryTestcaseCredential> getCredentials();

    public void setCredentials(@Nullable List<DiscoveryTestcaseCredential> creds);

    public boolean hasMailAddress();

    @Nullable
    public String getMailAddress();

    public void setMailAddress(@Nullable String mailAddr);

    public boolean isNegative();

    public void setNegative(boolean neg);
}
