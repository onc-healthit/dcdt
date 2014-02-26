package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultGenerator;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStep;
import java.util.List;

public abstract class AbstractToolTestcaseResultGenerator<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo> implements
    ToolTestcaseResultGenerator<T, U> {
    @Override
    public void generateTestcaseResult(List<ToolTestcaseResultStep> certDiscoverySteps) throws ToolTestcaseResultException {
    }

    @Override
    public int getErrorStepPosition(ToolTestcaseResult<T, U> testcaseResult) {
        return 0;
    }
}
