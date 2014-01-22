package gov.hhs.onc.dcdt.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import javax.annotation.Nullable;

public interface ToolTestcaseResult extends ToolNamedBean {
    @JsonProperty("passed")
    public boolean isPassed();

    public void setPassed(boolean passed);

    public boolean hasMessage();

    @JsonProperty("msg")
    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String message);
}
