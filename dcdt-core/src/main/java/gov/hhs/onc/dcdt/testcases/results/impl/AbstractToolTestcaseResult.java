package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractToolTestcaseResult<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo> extends AbstractToolNamedBean implements
    ToolTestcaseResult<T, U> {
    protected T resultConfig;
    protected U resultInfo;
    protected boolean successful;
    protected String message;

    @Override
    public boolean hasResultConfig() {
        return this.resultConfig != null;
    }

    @Nullable
    @Override
    public T getResultConfig() {
        return this.resultConfig;
    }

    @Override
    public void setResultConfig(@Nullable T resultConfig) {
        this.resultConfig = resultConfig;
    }

    @Override
    public boolean hasResultInfo() {
        return this.resultInfo != null;
    }

    @Nullable
    @Override
    public U getResultInfo() {
        return this.resultInfo;
    }

    @Override
    public void setResultInfo(@Nullable U resultInfo) {
        this.resultInfo = resultInfo;
    }

    @Override
    public boolean isSuccessful() {
        return this.successful;
    }

    @Override
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public boolean hasMessage() {
        return !StringUtils.isBlank(this.message);
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(@Nullable String message) {
        this.message = message;
    }
}
