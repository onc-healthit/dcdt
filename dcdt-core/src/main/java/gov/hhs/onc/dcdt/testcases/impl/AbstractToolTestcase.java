package gov.hhs.onc.dcdt.testcases.impl;


import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseResult;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractToolTestcase<T extends ToolTestcaseResult> extends AbstractToolBean implements ToolTestcase<T> {
    @Column(name = "name", nullable = false)
    @Id
    protected String name;

    @Transient
    protected String mailAddr;

    @Transient
    protected String nameDisplay;

    @Transient
    protected boolean optional;

    @Transient
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
    public boolean isOptional() {
        return this.optional;
    }

    @Override
    public void setOptional(boolean optional) {
        this.optional = optional;
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
