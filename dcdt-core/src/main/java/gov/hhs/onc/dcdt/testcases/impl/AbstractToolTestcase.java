package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import java.util.List;
import javax.annotation.Nullable;

public abstract class AbstractToolTestcase<T extends ToolTestcaseDescription> extends AbstractToolNamedBean implements ToolTestcase<T> {
    protected T desc;
    protected boolean neg;
    protected boolean optional;
    protected List<CertificateDiscoveryStep> steps;

    @Override
    public boolean hasDescription() {
        return this.desc != null;
    }

    @Nullable
    @Override
    public T getDescription() {
        return this.desc;
    }

    @Override
    public void setDescription(@Nullable T desc) {
        this.desc = desc;
    }

    @Nullable
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public boolean isNegative() {
        return this.neg;
    }

    @Override
    public void setNegative(boolean neg) {
        this.neg = neg;
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
    public boolean hasSteps() {
        return !this.steps.isEmpty();
    }

    @Override
    public List<CertificateDiscoveryStep> getSteps() {
        return this.steps;
    }

    @Override
    public void setSteps(List<CertificateDiscoveryStep> steps) {
        this.steps = steps;
    }
}
