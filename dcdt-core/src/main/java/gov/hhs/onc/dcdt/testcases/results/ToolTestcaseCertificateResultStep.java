package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.testcases.LocationType;
import javax.annotation.Nullable;

public interface ToolTestcaseCertificateResultStep extends ToolTestcaseResultStep {
    public boolean hasCertificateInfo();

    @JsonProperty("certInfo")
    @Nullable
    public CertificateInfo getCertificateInfo();

    public void setCertificateInfo(@Nullable CertificateInfo certificateInfo);

    @JsonProperty("certStatus")
    public ToolTestcaseCertificateResultType getCertificateStatus();

    public void setCertificateStatus(ToolTestcaseCertificateResultType certStatus);

    @JsonProperty("locationType")
    public LocationType getLocationType();

    public void setLocationType(LocationType locationType);

    @JsonProperty("bindingType")
    public BindingType getBindingType();

    public void setBindingType(BindingType bindingType);
}
