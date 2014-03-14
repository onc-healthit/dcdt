package gov.hhs.onc.dcdt.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;
import javax.annotation.Nullable;

public interface ToolTestcase<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo, V extends ToolTestcaseDescription, W extends ToolTestcaseResult<T, U>>
    extends ToolNamedBean {
    public boolean hasDescription();

    @JsonProperty("desc")
    @Nullable
    public V getDescription();

    public void setDescription(@Nullable V desc);

    @JsonProperty("neg")
    public boolean isNegative();

    public void setNegative(boolean neg);

    @JsonProperty("opt")
    public boolean isOptional();

    public void setOptional(boolean optional);

    public boolean hasResult();

    @JsonProperty("result")
    @Nullable
    public W getResult();

    public void setResult(@Nullable W result);
}
