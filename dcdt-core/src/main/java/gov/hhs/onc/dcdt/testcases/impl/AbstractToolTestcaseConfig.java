package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import java.util.List;
import javax.annotation.Nullable;

public abstract class AbstractToolTestcaseConfig extends AbstractToolBean implements ToolTestcaseConfig {
    protected List<ToolTestcaseStep> configSteps;

    @Override
    public boolean hasConfigSteps() {
        return this.configSteps != null;
    }

    @Nullable
    @Override
    public List<ToolTestcaseStep> getConfigSteps() {
        return this.configSteps;
    }

    @Override
    public void setConfigSteps(@Nullable List<ToolTestcaseStep> configSteps) {
        this.configSteps = configSteps;
    }
}
