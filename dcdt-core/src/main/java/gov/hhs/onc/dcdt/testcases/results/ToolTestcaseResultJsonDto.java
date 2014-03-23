package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import java.util.List;
import javax.annotation.Nullable;

public interface ToolTestcaseResultJsonDto<T extends ToolTestcaseResult> extends ToolBeanJsonDto<T> {
    @JsonProperty("successful")
    public boolean isSuccessful();

    public void setSuccessful(boolean successful);

    @JsonProperty("msg")
    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String message);

    @JsonProperty("certStr")
    @Nullable
    public String getCertificate();

    public void setCertificate(@Nullable String certStr);

    @JsonProperty("infoSteps")
    @Nullable
    public List<ToolTestcaseStep> getInfoSteps();

    public void setInfoSteps(@Nullable List<ToolTestcaseStep> infoSteps);
}
