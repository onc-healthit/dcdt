package gov.hhs.onc.dcdt.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import javax.annotation.Nullable;

public interface ToolTestcase<T extends ToolTestcaseDescription, U extends ToolTestcaseResult> extends ToolNamedBean {
    public boolean hasDescription();

    @JsonProperty("desc")
    @Nullable
    public T getDescription();

    public void setDescription(@Nullable T desc);

    @JsonProperty("neg")
    public boolean isNegative();

    public void setNegative(boolean neg);

    @JsonProperty("opt")
    public boolean isOptional();

    public void setOptional(boolean optional);

    public boolean hasResult();

    @JsonProperty("result")
    @Nullable
    public U getResult();

    public void setResult(@Nullable U result);
}
