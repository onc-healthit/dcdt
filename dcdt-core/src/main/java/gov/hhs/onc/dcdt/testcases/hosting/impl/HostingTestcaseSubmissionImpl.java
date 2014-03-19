package gov.hhs.onc.dcdt.testcases.hosting.impl;

import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseSubmission;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("hostingTestcaseSubmissionImpl")
@Lazy
@Scope("prototype")
public class HostingTestcaseSubmissionImpl extends AbstractToolTestcaseSubmission<HostingTestcaseDescription, HostingTestcaseConfig, HostingTestcase> implements
    HostingTestcaseSubmission {
}
