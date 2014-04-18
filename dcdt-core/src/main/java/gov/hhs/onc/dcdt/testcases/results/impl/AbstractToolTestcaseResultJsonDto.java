package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultJsonDto;
import java.util.List;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolTestcaseResultJsonDto<T extends ToolTestcaseDescription, U extends ToolTestcase<T>, V extends ToolTestcaseSubmission<T, U>, W extends ToolTestcaseResult<T, U, V>>
    extends AbstractToolBeanJsonDto<W> implements ToolTestcaseResultJsonDto<T, U, V, W> {
    protected List<String> msgs;
    protected List<CertificateDiscoveryStep> processedSteps;
    protected V submission;
    protected boolean success;

    protected AbstractToolTestcaseResultJsonDto(Class<W> beanClass, Class<? extends W> beanImplClass) {
        super(beanClass, beanImplClass);
    }

    @Override
    public boolean hasMessages() {
        return !this.getMessages().isEmpty();
    }

    @Override
    public List<String> getMessages() {
        return this.msgs;
    }

    @Override
    public void setMessages(List<String> msgs) {
        this.msgs = msgs;
    }

    @Override
    public List<CertificateDiscoveryStep> getProcessedSteps() {
        return this.processedSteps;
    }

    @Override
    public void setProcessedSteps(List<CertificateDiscoveryStep> processedSteps) {
        this.processedSteps = processedSteps;
    }

    @Override
    public V getSubmission() {
        return this.submission;
    }

    @Override
    public void setSubmission(V submission) {
        this.submission = submission;
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
