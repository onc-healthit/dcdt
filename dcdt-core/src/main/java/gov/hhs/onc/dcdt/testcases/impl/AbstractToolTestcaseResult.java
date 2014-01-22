package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseResult;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractToolTestcaseResult extends AbstractToolNamedBean implements ToolTestcaseResult {
    protected boolean passed;
    protected String message;

    @Override
    public boolean isPassed() {
        return this.passed;
    }

    @Override
    public void setPassed(boolean passed) {
        this.passed = passed;
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
