package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultDescriptor;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStep;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractToolTestcaseResultDescriptor extends AbstractToolBean implements ToolTestcaseResultDescriptor {
    protected List<ToolTestcaseResultStep> results;

    @Override
    public boolean hasResults() {
        return this.results != null;
    }

    @Nullable
    @Override
    public List<ToolTestcaseResultStep> getResults() {
        return this.results;
    }

    @Override
    public void setResults(@Nullable List<ToolTestcaseResultStep> results) {
        this.results = results;
    }
}
