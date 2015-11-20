package gov.hhs.onc.dcdt.testcases.hosting.results.impl;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.discovery.steps.CertificateValidationStep;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResult;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public class HostingTestcaseResultImpl extends AbstractToolTestcaseResult<HostingTestcaseDescription, HostingTestcase, HostingTestcaseSubmission> implements
    HostingTestcaseResult {
    public HostingTestcaseResultImpl(HostingTestcaseSubmission submission, @Nullable List<CertificateDiscoveryStep> procSteps) {
        super(submission, procSteps);
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
        return ToolCollectionUtils.addAll(new ArrayList<>(), super.getMessages(), this.getDiscoveryMessages());
    }

    @Override
    public boolean isSuccess() {
        // noinspection ConstantConditions
        return (this.submission.hasTestcase() && (this.submission.getTestcase().isNegative() != (this.isProcessingSuccess() && this.isDiscoverySuccess())));
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
}
