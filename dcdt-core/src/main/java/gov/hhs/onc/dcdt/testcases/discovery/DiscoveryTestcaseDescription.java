package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import java.util.List;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseDescription extends ToolTestcaseDescription {
    public boolean hasTargetCertificate();

    @Nullable
    public String getTargetCertificate();

    public void setTargetCertificate(@Nullable String targetCertificate);

    public boolean hasBackgroundCertificates();

    @Nullable
    public List<String> getBackgroundCertificates();

    public void setBackgroundCertificates(@Nullable List<String> backgroundCertificates);
}
