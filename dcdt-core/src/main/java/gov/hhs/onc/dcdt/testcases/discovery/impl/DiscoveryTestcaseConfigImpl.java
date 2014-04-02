package gov.hhs.onc.dcdt.testcases.discovery.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseConfig;

@JsonTypeName("discoveryTestcaseConfig")
public class DiscoveryTestcaseConfigImpl extends AbstractToolTestcaseConfig implements DiscoveryTestcaseConfig {
}
