package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import java.util.List;

public interface CertificateDiscoveryService extends ToolBean {
    public List<ToolTestcaseStep> runCertificateDiscoverySteps(MailAddress directAddr, List<ToolTestcaseStep> certDiscoverySteps)
        throws ToolTestcaseResultException;
}
