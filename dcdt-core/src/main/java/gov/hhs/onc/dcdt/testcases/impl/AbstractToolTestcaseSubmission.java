package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolDirectAddressBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import javax.annotation.Nullable;

public abstract class AbstractToolTestcaseSubmission<T extends ToolTestcaseDescription, U extends ToolTestcase<T>> extends AbstractToolDirectAddressBean
    implements ToolTestcaseSubmission<T, U> {
    protected U testcase;

    protected AbstractToolTestcaseSubmission(@Nullable U testcase) {
        this.testcase = testcase;
    }

    @Override
    public boolean hasTestcase() {
        return this.testcase != null;
    }

    @Nullable
    @Override
    public U getTestcase() {
        return this.testcase;
    }

    @Override
    public void setTestcase(@Nullable U testcase) {
        this.testcase = testcase;
    }
}
