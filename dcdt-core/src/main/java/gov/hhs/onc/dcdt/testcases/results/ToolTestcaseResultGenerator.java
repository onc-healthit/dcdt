package gov.hhs.onc.dcdt.testcases.results;

public interface ToolTestcaseResultGenerator<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo> {
    public boolean generateResultStatus(ToolTestcaseResult<T, U> testcaseResult);
}
