package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolResultBean;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

public abstract class AbstractToolTestcaseResult<T extends ToolTestcaseDescription, U extends ToolTestcase<T>, V extends ToolTestcaseSubmission<T, U>> extends
    AbstractToolResultBean implements ToolTestcaseResult<T, U, V> {
    protected List<CertificateDiscoveryStep> procSteps;
    protected List<ToolMessage> procMsgs = new ArrayList<>();
    protected Boolean procSuccess;
    protected V submission;

    protected AbstractToolTestcaseResult(V submission, @Nullable List<CertificateDiscoveryStep> procSteps) {
        this.submission = submission;

        // noinspection ConstantConditions
        this.procSteps = ToolCollectionUtils.addAll(new ArrayList<>(CollectionUtils.size(procSteps)), procSteps);
    }

    @Override
    public List<ToolMessage> getMessages() {
        return this.procMsgs;
    }

    @Override
    public boolean isSuccess() {
        // noinspection ConstantConditions
        return (this.submission.hasTestcase() && this.isProcessingSuccess());
    }

    @Override
    public boolean hasProcessedSteps() {
        return !this.procSteps.isEmpty();
    }

    @Override
    public List<CertificateDiscoveryStep> getProcessedSteps() {
        return this.procSteps;
    }

    @Override
    public boolean hasProcessingMessages() {
        return !this.getProcessingMessages().isEmpty();
    }

    @Override
    public List<ToolMessage> getProcessingMessages() {
        return procMsgs;
    }

    @Override
    public void setProcessingMessages(List<ToolMessage> procMsgs) {
        this.procMsgs = procMsgs;
    }

    @Override
    public boolean isProcessingSuccess() {
        return ObjectUtils.defaultIfNull(this.procSuccess, true);
    }

    @Override
    public void setProcessingSuccess(boolean procSuccess) {
        this.procSuccess = procSuccess;
    }

    @Override
    public V getSubmission() {
        return this.submission;
    }
}
