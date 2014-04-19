package gov.hhs.onc.dcdt.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import javax.annotation.Nullable;

public interface ToolTestcaseSubmissionJsonDto<T extends ToolTestcaseDescription, U extends ToolTestcase<T>, V extends ToolTestcaseSubmission<T, U>> extends
    ToolBeanJsonDto<V> {
    @JsonProperty("testcase")
    @Nullable
    public String getTestcase();

    public void setTestcase(@Nullable String testcase);
}
