package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import org.springframework.context.ApplicationContextAware;

public interface ToolTestcaseProcessor<T extends ToolTestcaseDescription, U extends ToolTestcase<T>, V extends ToolTestcaseSubmission<T, U>, W extends ToolTestcaseResult<T, U, V>>
    extends ApplicationContextAware, ToolBean {
    public W process(V submission);
}
