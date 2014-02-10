package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolDescriptionBean;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultDescription;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStep;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractToolTestcaseResultDescription extends AbstractToolDescriptionBean implements ToolTestcaseResultDescription {
    protected List<? extends ToolTestcaseResultStep> results;

    @Override
    public boolean hasResults() {
        return results != null;
    }

    @Nullable
    @Override
    public List<? extends ToolTestcaseResultStep> getResults() {
        return this.results;
    }

    @Override
    public void setResults(@Nullable List<? extends ToolTestcaseResultStep> results) {
        this.results = results;
    }
}
