package gov.hhs.onc.dcdt.testcases.hosting.results;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import java.util.List;
import javax.annotation.Nullable;

public interface HostingTestcaseResult extends ToolTestcaseResult<HostingTestcaseDescription, HostingTestcase, HostingTestcaseSubmission> {
    public boolean hasDiscoveredCertificateInfo();

    @Nullable
    public CertificateInfo getDiscoveredCertificateInfo();

    public boolean hasInvalidDiscoveredCertificateInfos();

    @Nullable
    public List<CertificateInfo> getInvalidDiscoveredCertificateInfos();

    public boolean hasDiscoveryMessages();

    public List<ToolMessage> getDiscoveryMessages();

    public boolean isDiscoverySuccess();
}
