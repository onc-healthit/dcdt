package gov.hhs.onc.dcdt.testcases.steps.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolDescriptionBean;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStepDescription;

@JsonTypeName("toolTestcaseStepDesc")
public class ToolTestcaseStepDescriptionImpl extends AbstractToolDescriptionBean implements ToolTestcaseStepDescription {
}
