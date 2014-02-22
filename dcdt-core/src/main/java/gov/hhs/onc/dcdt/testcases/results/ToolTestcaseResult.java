package gov.hhs.onc.dcdt.testcases.results;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import javax.annotation.Nullable;

public interface ToolTestcaseResult<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo> extends ToolNamedBean {
    public boolean hasResultConfig();

    @Nullable
    public T getResultConfig();

    public void setResultConfig(@Nullable T resultConfig);

    public boolean hasResultInfo();

    @Nullable
    public U getResultInfo();

    public void setResultInfo(@Nullable U resultInfo);

    public boolean isSuccessful();

    public void setSuccessful(boolean successful);

    public boolean hasMessage();

    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String message);
}
