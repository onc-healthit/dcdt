package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.crypto.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.data.tx.services.ToolBeanService;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseCredentialService extends ToolBeanService<DiscoveryTestcaseCredential, DiscoveryTestcaseCredentialDao> {
    public DiscoveryTestcaseCredential processDiscoveryTestcaseCredential(@Nullable CredentialInfo discoveryTestcaseCaCredInfo,
        DiscoveryTestcaseCredential discoveryTestcaseCred) throws CryptographyException;

    public CredentialInfo generateDiscoveryTestcaseCredentialInfo(@Nullable CredentialInfo discoveryTestcaseCaCredInfo,
        CredentialConfig discoveryTestcaseCredConfig) throws CryptographyException;
}
