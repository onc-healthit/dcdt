package gov.hhs.onc.dcdt.testcases.results;

import java.util.List;

public interface ToolTestcaseResultGenerator<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo> {
    public void generateTestcaseResult(List<ToolTestcaseResultStep> certDiscoverySteps) throws ToolTestcaseResultException;

    public int getErrorStepPosition(ToolTestcaseResult<T, U> testcaseResult);
}
