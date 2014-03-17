package gov.hhs.onc.dcdt.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import java.util.List;
import javax.annotation.Nullable;

public interface ToolTestcaseConfig extends ToolBean {
    public boolean hasConfigSteps();

    @JsonProperty("configSteps")
    @Nullable
    public List<ToolTestcaseStep> getConfigSteps();

    public void setConfigSteps(@Nullable List<ToolTestcaseStep> configSteps);
}
