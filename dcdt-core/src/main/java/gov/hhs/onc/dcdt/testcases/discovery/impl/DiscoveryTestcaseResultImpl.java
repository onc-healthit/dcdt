package gov.hhs.onc.dcdt.testcases.discovery.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseResult;

@JsonTypeName("discoveryTestcaseResult")
public class DiscoveryTestcaseResultImpl extends AbstractToolTestcaseResult implements DiscoveryTestcaseResult {
}
