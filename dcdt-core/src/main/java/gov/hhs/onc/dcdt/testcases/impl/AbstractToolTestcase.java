package gov.hhs.onc.dcdt.testcases.impl;


import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseResult;

public abstract class AbstractToolTestcase<T extends ToolTestcaseResult> extends AbstractToolBean implements ToolTestcase<T> {
    protected String mailAddr;
    protected String name;
    protected String nameDisplay;
    protected T result;

    @Override
    public String getMailAddress() {
        return this.mailAddr;
    }

    @Override
    public void setMailAddress(String mailAddr) {
        this.mailAddr = mailAddr;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getNameDisplay() {
        return this.nameDisplay;
    }

    @Override
    public void setNameDisplay(String nameDisplay) {
        this.nameDisplay = nameDisplay;
    }

    @Override
    public T getResult() {
        return this.result;
    }

    @Override
    public void setResult(T result) {
        this.result = result;
    }
}
