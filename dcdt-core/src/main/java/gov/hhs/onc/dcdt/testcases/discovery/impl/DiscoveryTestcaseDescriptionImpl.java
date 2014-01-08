package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseDescription;
import java.util.List;

public class DiscoveryTestcaseDescriptionImpl extends AbstractToolTestcaseDescription implements DiscoveryTestcaseDescription {
    private String targetCertificate;
    private List<String> backgroundCertificates;

    @Override
    public String getTargetCertificate() {
        return this.targetCertificate;
    }

    @Override
    public void setTargetCertificate(String targetCertificate) {
        this.targetCertificate = targetCertificate;
    }

    @Override
    public List<String> getBackgroundCertificates() {
        return this.backgroundCertificates;
    }

    @Override
    public void setBackgroundCertificates(List<String> backgroundCertificates) {
        this.backgroundCertificates = backgroundCertificates;
    }
}
