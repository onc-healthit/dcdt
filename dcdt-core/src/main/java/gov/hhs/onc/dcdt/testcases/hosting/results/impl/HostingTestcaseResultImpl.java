package gov.hhs.onc.dcdt.testcases.hosting.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResult;

@JsonTypeName("hostingTestcaseResult")
public class HostingTestcaseResultImpl extends AbstractToolTestcaseResult implements HostingTestcaseResult {
}