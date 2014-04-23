package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.discovery.CertificateDiscoveryService;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import java.util.List;
import javax.annotation.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public abstract class AbstractToolTestcaseProcessor<T extends ToolTestcaseDescription, U extends ToolTestcase<T>, V extends ToolTestcaseSubmission<T, U>, W extends ToolTestcaseResult<T, U, V>>
    extends AbstractToolBean implements ToolTestcaseProcessor<T, U, V, W> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected CertificateDiscoveryService certDiscoveryService;

    protected AbstractApplicationContext appContext;
    protected Class<W> resultClass;

    protected AbstractToolTestcaseProcessor(Class<W> resultClass) {
        this.resultClass = resultClass;
    }

    protected W createResult(V submission) {
        return this.createResult(submission, null);
    }

    protected W createResult(V submission, @Nullable List<CertificateDiscoveryStep> procSteps) {
        return ToolBeanFactoryUtils.createBeanOfType(this.appContext, this.resultClass, submission, procSteps);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }
}
