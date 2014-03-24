package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolDirectAddressBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import javax.annotation.Nullable;

public abstract class AbstractToolTestcaseSubmission<T extends ToolTestcaseDescription, U extends ToolTestcaseConfig, V extends ToolTestcase<T, U>> extends
    AbstractToolDirectAddressBean implements ToolTestcaseSubmission<T, U, V> {
    protected V testcase;

    @Override
    public boolean hasTestcase() {
        return this.testcase != null;
    }

    @Nullable
    @Override
    public V getTestcase() {
        return this.testcase;
    }

    @Override
    public void setTestcase(@Nullable V testcase) {
        this.testcase = testcase;
    }
}
