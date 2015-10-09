package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolResultBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.discovery.steps.CertificateValidationStep;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

public abstract class AbstractToolTestcaseResult<T extends ToolTestcaseDescription, U extends ToolTestcase<T>, V extends ToolTestcaseSubmission<T, U>>
    extends AbstractToolResultBean implements ToolTestcaseResult<T, U, V> {
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
    public boolean hasDiscoveredCertificateInfo() {
        return (this.getDiscoveredCertificateInfo() != null);
    }

    @Nullable
    @Override
    public CertificateInfo getDiscoveredCertificateInfo() {
        CertificateValidationStep processedCertValidationStep = ToolCollectionUtils.findAssignable(CertificateValidationStep.class, this.procSteps);

        return ((processedCertValidationStep != null) ? processedCertValidationStep.getValidCertificateInfo() : null);
    }

    @Override
    public boolean hasInvalidDiscoveredCertificateInfos() {
        return !CollectionUtils.isEmpty(this.getInvalidDiscoveredCertificateInfos());
    }

    @Nullable
    @Override
    public List<CertificateInfo> getInvalidDiscoveredCertificateInfos() {
        CertificateValidationStep processedCertValidationStep = ToolCollectionUtils.findAssignable(CertificateValidationStep.class, this.procSteps);

        return processedCertValidationStep != null ? processedCertValidationStep.getInvalidCertificateInfos() : null;
    }

    @Override
    public List<ToolMessage> getMessages() {
        return ToolCollectionUtils.addAll(new ArrayList<>(), this.procMsgs, this.getDiscoveryMessages());
    }

    @Override
    public boolean isSuccess() {
        // noinspection ConstantConditions
        return (this.submission.hasTestcase() && this.isProcessingSuccess() && this.isDiscoverySuccess());
    }

    @Override
    public boolean hasDiscoveryMessages() {
        return !this.getDiscoveryMessages().isEmpty();
    }

    @Override
    public List<ToolMessage> getDiscoveryMessages() {
        return this.procSteps.stream().flatMap(procStep -> procStep.getMessages().stream()).collect(Collectors.toList());
    }

    @Override
    public boolean isDiscoverySuccess() {
        // noinspection ConstantConditions
        return (this.hasProcessedSteps() && ToolListUtils.getLast(this.procSteps).isSuccess());
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
