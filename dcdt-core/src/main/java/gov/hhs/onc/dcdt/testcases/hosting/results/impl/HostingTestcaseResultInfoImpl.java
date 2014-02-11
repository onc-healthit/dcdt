package gov.hhs.onc.dcdt.testcases.hosting.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResultDescriptor;
import javax.annotation.Nullable;

@JsonTypeName("hostingTestcaseResultInfo")
public class HostingTestcaseResultInfoImpl extends AbstractToolTestcaseResultDescriptor implements HostingTestcaseResultInfo {
    private CertificateInfo certificateInfo;

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

}
