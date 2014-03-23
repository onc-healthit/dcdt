package gov.hhs.onc.dcdt.testcases.hosting.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseConfig;

@JsonTypeName("hostingTestcaseConfig")
public class HostingTestcaseConfigImpl extends AbstractToolTestcaseConfig implements HostingTestcaseConfig {
}
