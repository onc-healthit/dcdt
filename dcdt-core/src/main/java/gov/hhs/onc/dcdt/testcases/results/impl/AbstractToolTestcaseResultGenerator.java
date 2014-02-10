package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultGenerator;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;

public abstract class AbstractToolTestcaseResultGenerator<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo> implements ToolTestcaseResultGenerator<T, U> {
    @Override
    public boolean generateResultStatus(ToolTestcaseResult<T, U> testcaseResult) {
        //TODO: implement
        return false;
    }
}
