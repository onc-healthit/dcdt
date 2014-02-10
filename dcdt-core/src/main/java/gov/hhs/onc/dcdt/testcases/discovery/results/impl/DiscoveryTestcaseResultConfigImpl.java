package gov.hhs.onc.dcdt.testcases.discovery.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResultDescription;

@JsonTypeName("discoveryTestcaseResultConfig")
public class DiscoveryTestcaseResultConfigImpl extends AbstractToolTestcaseResultDescription implements DiscoveryTestcaseResultConfig {
}
