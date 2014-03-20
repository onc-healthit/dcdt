package gov.hhs.onc.dcdt.testcases.hosting;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseDescriptionImpl;

@JsonSubTypes({ @Type(HostingTestcaseDescriptionImpl.class) })
public interface HostingTestcaseDescription extends ToolTestcaseDescription {
}
