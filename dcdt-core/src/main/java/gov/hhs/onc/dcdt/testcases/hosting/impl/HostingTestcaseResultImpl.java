package gov.hhs.onc.dcdt.testcases.hosting.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseResult;

@JsonTypeName("hostingTestcaseResult")
public class HostingTestcaseResultImpl extends AbstractToolTestcaseResult implements HostingTestcaseResult {
}
