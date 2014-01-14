package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseDescription;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class DiscoveryTestcaseDescriptionImpl extends AbstractToolTestcaseDescription implements DiscoveryTestcaseDescription {
    private String targetCertificate;
    private List<String> backgroundCertificates;

    @Override
    public boolean hasTargetCertificate() {
        return !StringUtils.isBlank(this.targetCertificate);
    }

    @Nullable
    @Override
    public String getTargetCertificate() {
        return this.targetCertificate;
    }

    @Override
    public void setTargetCertificate(@Nullable String targetCertificate) {
        this.targetCertificate = targetCertificate;
    }

    @Override
    public boolean hasBackgroundCertificates() {
        return !CollectionUtils.isEmpty(this.backgroundCertificates);
    }

    @Nullable
    @Override
    public List<String> getBackgroundCertificates() {
        return this.backgroundCertificates;
    }

    @Override
    public void setBackgroundCertificates(@Nullable List<String> backgroundCertificates) {
        this.backgroundCertificates = backgroundCertificates;
    }
}
