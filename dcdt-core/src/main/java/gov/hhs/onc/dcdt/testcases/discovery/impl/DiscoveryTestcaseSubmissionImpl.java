package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseSubmission;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseSubmissionImpl")
@Lazy
@Scope("prototype")
public class DiscoveryTestcaseSubmissionImpl extends AbstractToolTestcaseSubmission<DiscoveryTestcaseDescription, DiscoveryTestcaseConfig, DiscoveryTestcase>
    implements DiscoveryTestcaseSubmission {
}
