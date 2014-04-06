package gov.hhs.onc.dcdt.testcases.hosting.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseCertificateStep;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

@Component("hostingTestcaseProcImpl")
public class HostingTestcaseProcessorImpl extends
    AbstractToolTestcaseProcessor<HostingTestcaseDescription, HostingTestcaseConfig, HostingTestcaseResult, HostingTestcase, HostingTestcaseSubmission>
    implements HostingTestcaseProcessor {
    @Override
    public HostingTestcaseResult generateTestcaseResult(HostingTestcaseSubmission submission, List<ToolTestcaseStep> certDiscoverySteps)
        throws ToolTestcaseResultException {
        MailAddress submissionDirectAddr = submission.getDirectAddress();
        List<ToolTestcaseStep> infoSteps = this.certDiscoveryService.discoverCertificates(submissionDirectAddr, certDiscoverySteps);
        ToolTestcaseStep lastInfoStep = ToolListUtils.getLast(infoSteps);

        HostingTestcase hostingTestcase = submission.getTestcase();

        HostingTestcaseResult hostingTestcaseResult = ToolBeanFactoryUtils.createBeanOfType(this.appContext, HostingTestcaseResult.class);
        // noinspection ConstantConditions
        hostingTestcaseResult.setInfoSteps(infoSteps);

        // noinspection ConstantConditions
        boolean hostingTestcaseNeg = hostingTestcase.isNegative(), hostingTestcaseSuccess = false;

        // noinspection ConstantConditions
        if (ToolClassUtils.isAssignable(lastInfoStep.getClass(), ToolTestcaseCertificateStep.class)) {
            ToolTestcaseCertificateStep certInfoStep = ((ToolTestcaseCertificateStep) lastInfoStep);

            // noinspection ConstantConditions
            if (((hostingTestcaseNeg && !certInfoStep.hasCertificateInfo() && (certInfoStep.getBindingType() == BindingType.DOMAIN)) || (!hostingTestcaseNeg
                && certInfoStep.hasCertificateInfo() && (hostingTestcase.getBindingType() == certInfoStep.getBindingType())))
                && (hostingTestcase.getLocationType() == certInfoStep.getLocationType())) {
                if (!hostingTestcaseNeg) {
                    try {
                        Pair<Boolean, List<String>> certInfoValidationResult =
                            this.certInfoValidator.validate(submissionDirectAddr, certInfoStep.getCertificateInfo());

                        if (certInfoValidationResult.getLeft()) {
                            // noinspection ConstantConditions
                            hostingTestcaseResult.setCertificate(certInfoStep.getCertificateInfo().getCertificate().toString());

                            hostingTestcaseSuccess = true;
                        }
                    } catch (Exception ignored) {
                        // TODO: implement
                    }
                } else {
                    hostingTestcaseSuccess = true;
                }
            }
        }

        hostingTestcaseResult.setSuccessful(hostingTestcaseSuccess);

        return hostingTestcaseResult;
    }
}
