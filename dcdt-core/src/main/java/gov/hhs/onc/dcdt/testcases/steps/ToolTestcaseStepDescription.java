package gov.hhs.onc.dcdt.testcases.steps;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolDescriptionBean;
import gov.hhs.onc.dcdt.testcases.steps.impl.ToolTestcaseStepDescriptionImpl;

@JsonSubTypes({ @Type(ToolTestcaseStepDescriptionImpl.class) })
public interface ToolTestcaseStepDescription extends ToolDescriptionBean {
}
