package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.CertificateDiscoveryService;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseDnsResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultHolder;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStep;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;
import java.util.List;

public class CertificateDiscoveryServiceImpl extends AbstractToolBean implements CertificateDiscoveryService {
    @Override
    public List<ToolTestcaseResultStep> runCertificateDiscoverySteps(MailAddress directAddr, ToolTestcaseResultHolder resultHolder,
        List<ToolTestcaseResultStep> certDiscoverySteps) throws ToolTestcaseResultException {
        List<ToolTestcaseResultStep> results = new ArrayList<>(certDiscoverySteps.size());

        for (ToolTestcaseResultStep resultStep : certDiscoverySteps) {
            ToolTestcaseResultStep newResultStep = copyResultStepProperties(resultStep);
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

    private ToolTestcaseResultStep copyResultStepProperties(ToolTestcaseResultStep resultStep) throws ToolTestcaseResultException {
        ToolTestcaseResultStep newResultStep;
        try {
            newResultStep = resultStep.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ToolTestcaseResultException(String.format("Unable to copy properties of result step (description=%s)", resultStep.getDescription()), e);
        }

        BeanUtils.copyProperties(resultStep, newResultStep);

        return newResultStep;
    }
}
