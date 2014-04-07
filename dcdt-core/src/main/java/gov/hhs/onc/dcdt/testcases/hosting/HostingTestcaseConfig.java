package gov.hhs.onc.dcdt.testcases.hosting;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseConfigImpl;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseConfig;

@JsonSubTypes({ @Type(HostingTestcaseConfigImpl.class) })
public interface HostingTestcaseConfig extends ToolTestcaseConfig {
}
