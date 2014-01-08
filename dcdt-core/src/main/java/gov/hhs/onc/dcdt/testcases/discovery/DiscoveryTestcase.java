package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import java.util.List;

public interface DiscoveryTestcase extends ToolTestcase<DiscoveryTestcaseDescription, DiscoveryTestcaseResult> {
    public List<DiscoveryTestcaseCredential> getCredentials();

    public void setCredentials(List<DiscoveryTestcaseCredential> creds);

    public String getMailAddress();

    public void setMailAddress(String mailAddr);
}
