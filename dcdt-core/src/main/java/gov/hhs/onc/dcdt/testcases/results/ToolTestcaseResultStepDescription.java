package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolDescriptionBean;
import gov.hhs.onc.dcdt.testcases.results.impl.ToolTestcaseResultStepDescriptionImpl;

@JsonSubTypes({ @Type(ToolTestcaseResultStepDescriptionImpl.class) })
public interface ToolTestcaseResultStepDescription extends ToolDescriptionBean {
}
