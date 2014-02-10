package gov.hhs.onc.dcdt.testcases.hosting.results;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultInfoImpl;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultDescription;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(HostingTestcaseResultInfoImpl.class) })
public interface HostingTestcaseResultInfo extends ToolTestcaseResultInfo, ToolTestcaseResultDescription {
    public boolean hasCertificateInfo();

    @Nullable
    public CertificateInfo getCertificateInfo();

    public void setCertificateInfo(@Nullable CertificateInfo certificateInfo);
}
