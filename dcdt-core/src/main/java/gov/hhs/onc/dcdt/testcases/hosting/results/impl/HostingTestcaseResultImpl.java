package gov.hhs.onc.dcdt.testcases.hosting.results.impl;

import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResult;
import java.util.List;
import javax.annotation.Nullable;

public class HostingTestcaseResultImpl extends AbstractToolTestcaseResult<HostingTestcaseDescription, HostingTestcase, HostingTestcaseSubmission> implements
    HostingTestcaseResult {
    public HostingTestcaseResultImpl(HostingTestcaseSubmission submission, @Nullable List<CertificateDiscoveryStep> procSteps) {
        super(submission, procSteps);
    }

    @Override
    public boolean isSuccess() {
        // noinspection ConstantConditions
        return (this.submission.hasTestcase() && (this.submission.getTestcase().isNegative() != (this.isProcessingSuccess() && this.isDiscoverySuccess())));
    }
}
