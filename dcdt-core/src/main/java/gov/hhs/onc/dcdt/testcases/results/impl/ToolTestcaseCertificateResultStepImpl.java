package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.testcases.LocationType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import javax.annotation.Nullable;

public class ToolTestcaseCertificateResultStepImpl extends AbstractToolTestcaseResultStep implements ToolTestcaseCertificateResultStep {
    private CertificateInfo certificateInfo;
    private ToolTestcaseCertificateResultType certStatus;
    private LocationType locationType;
    private BindingType bindingType;

    @Override
    public boolean hasCertificateInfo() {
        return this.certificateInfo != null;
    }

    @Nullable
    @Override
    public CertificateInfo getCertificateInfo() {
        return this.certificateInfo;
    }

    @Override
    public void setCertificateInfo(@Nullable CertificateInfo certificateInfo) {
        this.certificateInfo = certificateInfo;
    }

    @Override
    public ToolTestcaseCertificateResultType getCertStatus() {
        return this.certStatus;
    }

    @Override
    public void setCertStatus(ToolTestcaseCertificateResultType certStatus) {
        this.certStatus = certStatus;
    }

    @Override
    public LocationType getLocationType() {
        return this.locationType;
    }

    @Override
    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    @Override
    public BindingType getBindingType() {
        return this.bindingType;
    }

    @Override
    public void setBindingType(BindingType bindingType) {
        this.bindingType = bindingType;
    }
}
