package gov.hhs.onc.dcdt.testcases.discovery;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.discovery.impl.DiscoveryTestcaseConfigImpl;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseConfig;

@JsonSubTypes({ @Type(DiscoveryTestcaseConfigImpl.class) })
public interface DiscoveryTestcaseConfig extends ToolTestcaseConfig {
}
