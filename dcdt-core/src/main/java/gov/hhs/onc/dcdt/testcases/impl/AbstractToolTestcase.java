package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseResult;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractToolTestcase<T extends ToolTestcaseDescription, U extends ToolTestcaseResult> extends AbstractToolBean implements
    ToolTestcase<T, U> {
    @Column(name = "name", nullable = false)
    @Id
    protected String name;

    @Transient
    protected String nameDisplay;

    @Transient
    protected boolean optional;

    @Transient
    protected T desc;

    @Transient
    protected U result;

    @Override
    public T getDescription() {
        return this.desc;
    }

    @Override
    public void setDescription(T desc) {
        this.desc = desc;
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
    public U getResult() {
        return this.result;
    }

    @Override
    public void setResult(U result) {
        this.result = result;
    }
}
