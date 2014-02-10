package gov.hhs.onc.dcdt.testcases.hosting.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResultDescription;

@JsonTypeName("hostingTestcaseResultConfig")
public class HostingTestcaseResultConfigImpl extends AbstractToolTestcaseResultDescription implements HostingTestcaseResultConfig {
}
