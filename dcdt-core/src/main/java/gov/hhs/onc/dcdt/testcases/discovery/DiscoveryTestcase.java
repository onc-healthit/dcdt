package gov.hhs.onc.dcdt.testcases.discovery;


import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import java.util.List;

public interface DiscoveryTestcase extends ToolTestcase<DiscoveryTestcaseResult> {
    public List<DiscoveryTestcaseCertificate> getCertificates();

    public void setCertificates(List<DiscoveryTestcaseCertificate> certs);
}
