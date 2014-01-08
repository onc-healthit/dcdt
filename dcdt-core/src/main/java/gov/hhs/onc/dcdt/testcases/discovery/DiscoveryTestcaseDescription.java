package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import java.util.List;

public interface DiscoveryTestcaseDescription extends ToolTestcaseDescription {
    public String getTargetCertificate();

    public void setTargetCertificate(String targetCertificate);

    public List<String> getBackgroundCertificates();

    public void setBackgroundCertificates(List<String> backgroundCertificates);
}
