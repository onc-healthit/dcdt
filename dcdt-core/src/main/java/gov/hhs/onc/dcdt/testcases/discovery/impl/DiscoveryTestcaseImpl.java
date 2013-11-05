package gov.hhs.onc.dcdt.testcases.discovery.impl;


import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCertificate;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcase;
import java.util.List;

public class DiscoveryTestcaseImpl extends AbstractToolTestcase<DiscoveryTestcaseResult> implements DiscoveryTestcase {
    private List<DiscoveryTestcaseCertificate> certs;
    private String mailAddr;

    @Override
    public List<DiscoveryTestcaseCertificate> getCertificates() {
        return this.certs;
    }

    @Override
    public void setCertificates(List<DiscoveryTestcaseCertificate> certs) {
        this.certs = certs;
    }

    @Override
    public String getMailAddress() {
        return this.mailAddr;
    }

    @Override
    public void setMailAddress(String mailAddr) {
        this.mailAddr = mailAddr;
    }
}
