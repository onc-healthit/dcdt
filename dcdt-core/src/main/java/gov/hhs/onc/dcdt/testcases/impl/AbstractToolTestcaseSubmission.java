package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import javax.annotation.Nullable;

public abstract class AbstractToolTestcaseSubmission<T extends ToolTestcaseDescription, U extends ToolTestcaseConfig, V extends ToolTestcase<T, U>> extends
    AbstractToolBean implements ToolTestcaseSubmission<T, U, V> {
    protected MailAddress directAddr;
    protected V testcase;

    @Override
    public boolean hasDirectAddress() {
        return this.directAddr != null;
    }

    @Nullable
    @Override
    public MailAddress getDirectAddress() {
        return this.directAddr;
    }

    @Override
    public void setDirectAddress(@Nullable MailAddress directAddr) {
        this.directAddr = directAddr;
    }

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
