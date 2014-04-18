package gov.hhs.onc.dcdt.discovery.steps;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import javax.annotation.Nullable;

public interface CertificateValidationStep extends CertificateDiscoveryStep {
    public boolean hasValidCertificateInfo();

    @Nullable
    public CertificateInfo getValidCertificateInfo();
}
