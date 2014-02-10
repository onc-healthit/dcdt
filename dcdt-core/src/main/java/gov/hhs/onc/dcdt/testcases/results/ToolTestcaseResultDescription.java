package gov.hhs.onc.dcdt.testcases.results;

import gov.hhs.onc.dcdt.beans.ToolDescriptionBean;
import javax.annotation.Nullable;
import java.util.List;

public interface ToolTestcaseResultDescription extends ToolDescriptionBean {
    public boolean hasResults();

    @Nullable
    public List<? extends ToolTestcaseResultStep> getResults();

    public void setResults(@Nullable List<? extends ToolTestcaseResultStep> results);
}
