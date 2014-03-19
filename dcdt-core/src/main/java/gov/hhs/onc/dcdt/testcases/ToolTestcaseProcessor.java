package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import java.util.List;
import org.springframework.context.ApplicationContextAware;

public interface ToolTestcaseProcessor<T extends ToolTestcaseDescription, U extends ToolTestcaseConfig, V extends ToolTestcaseResult, W extends ToolTestcase<T, U>, X extends ToolTestcaseSubmission<T, U, W>>
    extends ApplicationContextAware {
    public V generateTestcaseResult(X submission, List<ToolTestcaseStep> certDiscoverySteps) throws ToolTestcaseResultException;

    public int getErrorStepPosition(U config, V result);
}
