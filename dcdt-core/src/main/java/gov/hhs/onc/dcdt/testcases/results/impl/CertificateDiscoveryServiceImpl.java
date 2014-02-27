package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanPropertyUtils;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanUtils;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.CertificateDiscoveryService;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseDnsResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultHolder;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStep;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component("certificateDiscoveryServiceImpl")
public class CertificateDiscoveryServiceImpl extends AbstractToolBean implements CertificateDiscoveryService {
    private AbstractApplicationContext appContext;

    @Override
    public List<ToolTestcaseResultStep> runCertificateDiscoverySteps(MailAddress directAddr, ToolTestcaseResultHolder resultHolder,
        List<ToolTestcaseResultStep> certDiscoverySteps) throws ToolTestcaseResultException {
        List<ToolTestcaseResultStep> results = new ArrayList<>(certDiscoverySteps.size());

        for (ToolTestcaseResultStep resultStep : certDiscoverySteps) {
            ToolTestcaseResultStep newResultStep = ToolBeanFactoryUtils.createBeanOfType(this.appContext, resultStep.getClass());
            ToolBeanPropertyUtils.copy(ToolBeanUtils.wrap(resultStep), ToolBeanUtils.wrap(newResultStep));
            results.add(newResultStep);

            boolean successful = newResultStep.execute(resultHolder, directAddr);
            newResultStep.setSuccessful(successful);

            switch (newResultStep.getResultType()) {
                case CERT_LOOKUP:
                    ToolTestcaseCertificateResultStep certResultStep = ((ToolTestcaseCertificateResultStep) newResultStep);
                    if (successful && certResultStep.hasCertificateInfo()
                        || (!certResultStep.hasCertificateInfo() && certResultStep.getCertificateStatus() != ToolTestcaseCertificateResultType.NO_CERT)) {
                        return results;
                    }
                    break;
                case DNS_LOOKUP:
                    if (!successful && ((ToolTestcaseDnsResultStep) resultStep).getDnsRecordType() == DnsRecordType.SRV) {
                        return results;
                    }
                    break;
                case LDAP_CONNECTION:
                    if (!successful) {
                        return results;
                    }
                    break;
                default:
                    break;
            }
        }
        return results;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) applicationContext;
    }
}
