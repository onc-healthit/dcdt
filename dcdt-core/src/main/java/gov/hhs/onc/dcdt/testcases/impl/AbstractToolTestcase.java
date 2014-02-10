package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractToolTestcase<T extends ToolTestcaseDescription, U extends ToolTestcaseResult<?, ?>> extends AbstractToolNamedBean implements
    ToolTestcase<T, U> {
    protected T desc;
    protected boolean neg;
    protected boolean optional;
    protected U result;

    @Override
    public boolean hasDescription() {
        return this.desc != null;
    }

    @Nullable
    @Override
    @Transient
    public T getDescription() {
        return this.desc;
    }

    @Override
    public void setDescription(@Nullable T desc) {
        this.desc = desc;
    }

    @Column(name = "name", nullable = false)
    @Id
    @Nullable
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    @Transient
    public boolean isNegative() {
        return this.neg;
    }

    @Override
    public void setNegative(boolean neg) {
        this.neg = neg;
    }

    @Override
    @Transient
    public boolean isOptional() {
        return this.optional;
    }

    @Override
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    @Override
    public boolean hasResult() {
        return this.result != null;
    }

    @Override
    @Nullable
    @Transient
    public U getResult() {
        return this.result;
    }

    @Override
    public void setResult(@Nullable U result) {
        this.result = result;
    }
}
