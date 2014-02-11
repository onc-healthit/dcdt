package gov.hhs.onc.dcdt.testcases.hosting.results;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultConfigImpl;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultConfig;

@JsonSubTypes({ @Type(HostingTestcaseResultConfigImpl.class) })
public interface HostingTestcaseResultConfig extends ToolTestcaseResultConfig {
}
