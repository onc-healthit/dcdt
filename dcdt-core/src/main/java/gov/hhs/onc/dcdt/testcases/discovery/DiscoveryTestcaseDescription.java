package gov.hhs.onc.dcdt.testcases.discovery;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.impl.DiscoveryTestcaseDescriptionImpl;

@JsonSubTypes({ @Type(DiscoveryTestcaseDescriptionImpl.class) })
public interface DiscoveryTestcaseDescription extends ToolTestcaseDescription {
}
