package gov.hhs.onc.dcdt.testcases.discovery.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseDescription;

@JsonTypeName("discoveryTestcaseDesc")
public class DiscoveryTestcaseDescriptionImpl extends AbstractToolTestcaseDescription implements DiscoveryTestcaseDescription {
}
