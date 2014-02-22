package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolBean;
import javax.annotation.Nullable;

public interface ToolTestcaseResultStep extends ToolBean {
    public boolean hasDescription();

    @JsonProperty("desc")
    @Nullable
    public ToolTestcaseResultStepDescription getDescription();

    public void setDescription(@Nullable ToolTestcaseResultStepDescription description);

    @JsonProperty("resultType")
    public ToolTestcaseResultType getResultType();

    public void setResultType(ToolTestcaseResultType resultType);

    @JsonProperty("successful")
    public boolean isSuccessful();

    public void setSuccessful(boolean successful);

    public boolean hasMessage();

    @JsonProperty("msg")
    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String message);
}
