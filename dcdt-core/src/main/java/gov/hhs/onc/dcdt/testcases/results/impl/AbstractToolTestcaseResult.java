package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;
import javax.annotation.Nullable;

public abstract class AbstractToolTestcaseResult<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo> extends AbstractToolNamedBean implements
    ToolTestcaseResult<T, U> {
    protected T resultConfig;
    protected U resultInfo;

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
}
