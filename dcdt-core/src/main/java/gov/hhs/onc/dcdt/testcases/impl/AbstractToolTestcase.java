package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractToolTestcase<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo, V extends ToolTestcaseDescription, W extends ToolTestcaseResult<T, U>>
    extends AbstractToolNamedBean implements ToolTestcase<T, U, V, W> {
    protected V desc;
    protected boolean neg;
    protected boolean optional;
    protected W result;

    @Override
    public boolean hasDescription() {
        return this.desc != null;
    }

    @Nullable
    @Override
    @Transient
    public V getDescription() {
        return this.desc;
    }

    @Override
    public void setDescription(@Nullable V desc) {
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
    public W getResult() {
        return this.result;
    }

    @Override
    public void setResult(@Nullable W result) {
        this.result = result;
    }
}
