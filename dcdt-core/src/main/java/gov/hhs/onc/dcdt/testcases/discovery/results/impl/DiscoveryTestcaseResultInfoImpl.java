package gov.hhs.onc.dcdt.testcases.discovery.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResultDescriptor;

@JsonTypeName("discoveryTestcaseResultInfo")
public class DiscoveryTestcaseResultInfoImpl extends AbstractToolTestcaseResultDescriptor implements DiscoveryTestcaseResultInfo {
}
