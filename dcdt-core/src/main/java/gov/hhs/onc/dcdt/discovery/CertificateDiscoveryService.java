package gov.hhs.onc.dcdt.discovery;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.List;

public interface CertificateDiscoveryService extends ToolBean {
    public List<CertificateDiscoveryStep> discoverCertificates(MailAddress directAddr);

    public List<CertificateDiscoveryStep> discoverCertificates(List<CertificateDiscoveryStep> steps, MailAddress directAddr);
}
