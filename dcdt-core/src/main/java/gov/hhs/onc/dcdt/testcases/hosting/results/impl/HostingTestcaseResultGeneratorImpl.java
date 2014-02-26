package gov.hhs.onc.dcdt.testcases.hosting.results.impl;

import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultGenerator;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.results.CertificateDiscoveryService;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.results.impl.CertificateDiscoveryServiceImpl;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStep;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResultGenerator;
import gov.hhs.onc.dcdt.testcases.results.impl.ToolTestcaseResultHolderImpl;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.util.List;

public class HostingTestcaseResultGeneratorImpl extends AbstractToolTestcaseResultGenerator<HostingTestcaseResultConfig, HostingTestcaseResultInfo> implements
    HostingTestcaseResultGenerator {
    private HostingTestcaseSubmission submission;
    private CertificateDiscoveryService certDiscoveryService = new CertificateDiscoveryServiceImpl();

    public HostingTestcaseResultGeneratorImpl(HostingTestcaseSubmission submission) {
        this.submission = submission;
    }

    @Override
    public void generateTestcaseResult(List<ToolTestcaseResultStep> certDiscoverySteps) throws ToolTestcaseResultException {
        HostingTestcase hostingTestcase = this.submission.getHostingTestcase();
        HostingTestcaseResult hostingTestcaseResult = hostingTestcase.getResult();
        HostingTestcaseResultInfo resultInfo = new HostingTestcaseResultInfoImpl();

        List<ToolTestcaseResultStep> results =
            certDiscoveryService.runCertificateDiscoverySteps(this.submission.getDirectAddress(), new ToolTestcaseResultHolderImpl(), certDiscoverySteps);
        ToolTestcaseResultStep lastStep = ToolListUtils.getLast(results);

        boolean isSuccessful = false;

        if (lastStep instanceof ToolTestcaseCertificateResultStep) {
            ToolTestcaseCertificateResultStep certResultStep = ((ToolTestcaseCertificateResultStep) lastStep);

            isSuccessful = foundExpectedCertificate(hostingTestcase, certResultStep) || foundNoCertificate(hostingTestcase, certResultStep);

            if (certResultStep.hasCertificateInfo()) {
                resultInfo.setCertificate(certResultStep.getCertificateInfo().getCertificate().toString());
            }
        }

        resultInfo.setResults(results);
        resultInfo.setSuccessful(isSuccessful);
        hostingTestcaseResult.setResultInfo(resultInfo);
    }

    @Override
    public int getErrorStepPosition(ToolTestcaseResult<HostingTestcaseResultConfig, HostingTestcaseResultInfo> testcaseResult) {
        int stepPosition = 0;

        HostingTestcaseResultInfo resultInfo = testcaseResult.getResultInfo();
        if (resultInfo.isSuccessful()) {
            return stepPosition;
        }

        List<ToolTestcaseResultStep> resultConfigSteps = testcaseResult.getResultConfig().getResults();
        List<ToolTestcaseResultStep> resultInfoSteps = resultInfo.getResults();

        for (stepPosition = 0; stepPosition < resultConfigSteps.size(); stepPosition++) {
            if (stepPosition < resultInfoSteps.size()) {
                ToolTestcaseResultStep resultConfigStep = resultConfigSteps.get(stepPosition);
                ToolTestcaseResultStep resultInfoStep = resultInfoSteps.get(stepPosition);

                if (resultConfigStep.isSuccessful() != resultInfoStep.isSuccessful()) {
                    return stepPosition + 1;
                }
            } else {
                return stepPosition + 1;
            }
        }
        return stepPosition;
    }

    private boolean foundExpectedCertificate(HostingTestcase hostingTestcase, ToolTestcaseCertificateResultStep certResultStep) {
        return certResultStep.hasCertificateInfo() && !hostingTestcase.isNegative() && hostingTestcase.getBindingType() == certResultStep.getBindingType()
            && hostingTestcase.getLocationType() == certResultStep.getLocationType();
    }

    private boolean foundNoCertificate(HostingTestcase hostingTestcase, ToolTestcaseCertificateResultStep certResultStep) {
        return !certResultStep.hasCertificateInfo() && hostingTestcase.isNegative() && hostingTestcase.getLocationType() == certResultStep.getLocationType()
            && certResultStep.getBindingType() == BindingType.DOMAIN;
    }
}
