package gov.hhs.onc.dcdt.testcases.hosting.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.CertificateDiscoveryService;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseCertificateStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseProcessor;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component("hostingTestcaseProcessorImpl")
public class HostingTestcaseProcessorImpl extends
    AbstractToolTestcaseProcessor<HostingTestcaseDescription, HostingTestcaseConfig, HostingTestcaseResult, HostingTestcase, HostingTestcaseSubmission>
    implements HostingTestcaseProcessor {
    @Autowired
    private CertificateDiscoveryService certDiscoveryService;

    @Override
    public HostingTestcaseResult generateTestcaseResult(HostingTestcaseSubmission submission, List<ToolTestcaseStep> certDiscoverySteps)
        throws ToolTestcaseResultException {
        HostingTestcaseResult hostingTestcaseResult = ToolBeanFactoryUtils.createBeanOfType(this.appContext, HostingTestcaseResult.class);
        List<ToolTestcaseStep> infoSteps = certDiscoveryService.runCertificateDiscoverySteps(submission.getDirectAddress(), certDiscoverySteps);
        // noinspection ConstantConditions
        hostingTestcaseResult.setInfoSteps(infoSteps);
        hostingTestcaseResult.setSuccessful(getResult(submission.getTestcase(), hostingTestcaseResult, ToolListUtils.getLast(infoSteps)));

        return hostingTestcaseResult;
    }

    private boolean getResult(HostingTestcase testcase, HostingTestcaseResult result, ToolTestcaseStep lastStep) {
        boolean isSuccessful = false;

        if (lastStep instanceof ToolTestcaseCertificateStep) {
            ToolTestcaseCertificateStep certInfoStep = ((ToolTestcaseCertificateStep) lastStep);

            isSuccessful = foundExpectedCertificate(testcase, certInfoStep) || foundNoCertificate(testcase, certInfoStep);

            if (certInfoStep.hasCertificateInfo()) {
                // noinspection ConstantConditions
                result.setCertificate(certInfoStep.getCertificateInfo().getCertificate().toString());
            }
        }
        return isSuccessful;
    }

    private boolean foundExpectedCertificate(HostingTestcase hostingTestcase, ToolTestcaseCertificateStep certInfoStep) {
        return certInfoStep.hasCertificateInfo() && !hostingTestcase.isNegative() && hostingTestcase.getBindingType() == certInfoStep.getBindingType()
            && hostingTestcase.getLocationType() == certInfoStep.getLocationType();
    }

    private boolean foundNoCertificate(HostingTestcase hostingTestcase, ToolTestcaseCertificateStep certInfoStep) {
        return !certInfoStep.hasCertificateInfo() && hostingTestcase.isNegative() && hostingTestcase.getLocationType() == certInfoStep.getLocationType()
            && certInfoStep.getBindingType() == BindingType.DOMAIN;
    }
}
