package gov.hhs.onc.dcdt.testcases.hosting.impl;

import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseProcessor;
import org.springframework.stereotype.Component;

@Component("hostingTestcaseProcImpl")
public class HostingTestcaseProcessorImpl extends
    AbstractToolTestcaseProcessor<HostingTestcaseDescription, HostingTestcase, HostingTestcaseSubmission, HostingTestcaseResult> implements
    HostingTestcaseProcessor {
    public HostingTestcaseProcessorImpl() {
        super(HostingTestcaseResult.class);
    }

    @Override
    public HostingTestcaseResult process(HostingTestcaseSubmission submission) {
        HostingTestcase testcase = submission.getTestcase();

        // noinspection ConstantConditions
        return this.createResult(submission, this.certDiscoveryService.discoverCertificates(testcase.getSteps(), submission.getDirectAddress()));
    }
}
