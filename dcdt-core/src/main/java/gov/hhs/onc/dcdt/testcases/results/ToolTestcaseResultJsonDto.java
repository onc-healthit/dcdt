package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import javax.annotation.Nullable;

public interface ToolTestcaseResultJsonDto<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo, V extends ToolTestcaseResult<T, U>> extends
    ToolBeanJsonDto<V> {

    public boolean hasResultConfig();

    @JsonProperty("resultConfig")
    @Nullable
    public T getResultConfig();

    public void setResultConfig(@Nullable T resultConfig);

    public boolean hasResultInfo();

    @JsonProperty("resultInfo")
    @Nullable
    public U getResultInfo();

    public void setResultInfo(@Nullable U resultInfo);

    @JsonProperty("successful")
    public boolean isSuccessful();

    public void setSuccessful(boolean successful);

    public boolean hasMessage();

    @JsonProperty("msg")
    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String message);
}
