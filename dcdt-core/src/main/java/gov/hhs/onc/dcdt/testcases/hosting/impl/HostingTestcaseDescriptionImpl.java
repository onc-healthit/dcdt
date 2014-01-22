package gov.hhs.onc.dcdt.testcases.hosting.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseDescription;

@JsonTypeName("hostingTestcaseDesc")
public class HostingTestcaseDescriptionImpl extends AbstractToolTestcaseDescription implements HostingTestcaseDescription {
}
