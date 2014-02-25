package gov.hhs.onc.dcdt.testcases.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolDescriptionBean;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStepDescription;

@JsonTypeName("toolTestcaseResultStepDesc")
public class ToolTestcaseResultStepDescriptionImpl extends AbstractToolDescriptionBean implements ToolTestcaseResultStepDescription {
}
