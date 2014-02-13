package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolBean;
import javax.annotation.Nullable;
import java.util.List;

public interface ToolTestcaseResultDescriptor extends ToolBean {
    public boolean hasResults();

    @JsonProperty("results")
    @Nullable
    public List<ToolTestcaseResultStep> getResults();

    public void setResults(@Nullable List<ToolTestcaseResultStep> results);
}
