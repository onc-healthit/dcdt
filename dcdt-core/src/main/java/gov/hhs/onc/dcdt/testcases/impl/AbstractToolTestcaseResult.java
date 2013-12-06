package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseResult;

public abstract class AbstractToolTestcaseResult extends AbstractToolBean implements ToolTestcaseResult {
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
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
