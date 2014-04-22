package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultJsonDto;
import java.util.List;

public abstract class AbstractToolTestcaseResultJsonDto<T extends ToolTestcaseDescription, U extends ToolTestcase<T>, V extends ToolTestcaseSubmission<T, U>, W extends ToolTestcaseResult<T, U, V>>
    extends AbstractToolBeanJsonDto<W> implements ToolTestcaseResultJsonDto<T, U, V, W> {
    protected CertificateInfo discoveredCertInfo;
    protected List<String> msgs;
    protected List<String> procMsgs;
    protected List<CertificateDiscoveryStep> procSteps;
    protected V submission;
    protected boolean success;

    protected AbstractToolTestcaseResultJsonDto(Class<W> beanClass, Class<? extends W> beanImplClass) {
        super(beanClass, beanImplClass);
    }

    @Override
    public CertificateInfo getDiscoveredCertificateInfo() {
        return this.discoveredCertInfo;
    }

    @Override
    public void setDiscoveredCertificateInfo(CertificateInfo discoveredCertInfo) {
        this.discoveredCertInfo = discoveredCertInfo;
    }

    @Override
    public boolean hasMessages() {
        return !this.msgs.isEmpty();
    }

    @Override
    public List<String> getMessages() {
        return msgs;
    }

    @Override
    public void setMessages(List<String> msgs) {
        this.msgs = msgs;
    }

    @Override
    public List<CertificateDiscoveryStep> getProcessedSteps() {
        return this.procSteps;
    }

    @Override
    public void setProcessedSteps(List<CertificateDiscoveryStep> procSteps) {
        this.procSteps = procSteps;
    }

    @Override
    public boolean hasProcessingMessages() {
        return !this.procMsgs.isEmpty();
    }

    @Override
    public List<String> getProcessingMessages() {
        return procMsgs;
    }

    @Override
    public void setProcessingMessages(List<String> procMsgs) {
        this.procMsgs = procMsgs;
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
