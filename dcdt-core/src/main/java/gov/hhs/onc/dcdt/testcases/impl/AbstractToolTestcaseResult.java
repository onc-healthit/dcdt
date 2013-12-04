package gov.hhs.onc.dcdt.testcases.impl;


import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseResult;

public abstract class AbstractToolTestcaseResult extends AbstractToolBean implements ToolTestcaseResult {
    protected boolean passed;

    @Override
    public boolean isPassed() {
        return this.passed;
    }

    @Override
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
