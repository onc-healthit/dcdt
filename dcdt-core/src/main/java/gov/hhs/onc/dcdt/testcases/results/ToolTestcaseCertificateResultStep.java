package gov.hhs.onc.dcdt.testcases.results;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.testcases.LocationType;
import javax.annotation.Nullable;

public interface ToolTestcaseCertificateResultStep extends ToolTestcaseResultStep {
    public boolean hasCertificateInfo();

    @Nullable
    public CertificateInfo getCertificateInfo();

    public void setCertificateInfo(@Nullable CertificateInfo certificateInfo);

    public ToolTestcaseCertificateResultType getCertificateStatus();

    public void setCertificateStatus(ToolTestcaseCertificateResultType certStatus);

    public LocationType getLocationType();

    public void setLocationType(LocationType locationType);

    public BindingType getBindingType();

    public void setBindingType(BindingType bindingType);
}
