package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import java.util.List;
import org.springframework.context.ApplicationContextAware;

public interface CertificateDiscoveryService extends ApplicationContextAware, ToolBean {
    public List<ToolTestcaseStep> discoverCertificates(MailAddress directAddr) throws ToolTestcaseResultException;

    public List<ToolTestcaseStep> discoverCertificates(MailAddress directAddr, List<ToolTestcaseStep> certDiscoverySteps) throws ToolTestcaseResultException;
}
