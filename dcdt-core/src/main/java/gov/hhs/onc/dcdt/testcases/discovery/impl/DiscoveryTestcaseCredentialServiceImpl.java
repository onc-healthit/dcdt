package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.crypto.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.KeyUtils;
import gov.hhs.onc.dcdt.data.tx.services.impl.AbstractToolBeanService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredentialDao;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredentialService;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("discoveryTestcaseCredServiceImpl")
public class DiscoveryTestcaseCredentialServiceImpl extends AbstractToolBeanService<DiscoveryTestcaseCredential, DiscoveryTestcaseCredentialDao> implements
    DiscoveryTestcaseCredentialService {
    @Override
    public DiscoveryTestcaseCredential processDiscoveryTestcaseCredential(@Nullable CredentialInfo discoveryTestcaseCaCredInfo,
        DiscoveryTestcaseCredential discoveryTestcaseCred) throws CryptographyException {
        String discoveryTestcaseCredName = discoveryTestcaseCred.getName();
        DiscoveryTestcaseCredential discoveryTestcaseCredExisting = this.containsBean(discoveryTestcaseCredName)
            ? this.getBeanById(discoveryTestcaseCredName) : null;

        if (discoveryTestcaseCredExisting != null) {
            discoveryTestcaseCred.setCredentialInfo(discoveryTestcaseCredExisting.getCredentialInfo());
        } else {
            discoveryTestcaseCred.setCredentialInfo(this.generateDiscoveryTestcaseCredentialInfo(discoveryTestcaseCaCredInfo,
                discoveryTestcaseCred.getCredentialConfig()));
        }

        this.setBean(discoveryTestcaseCred);

        return discoveryTestcaseCred;
    }

    @Override
    public CredentialInfo generateDiscoveryTestcaseCredentialInfo(@Nullable CredentialInfo discoveryTestcaseCaCredInfo,
        CredentialConfig discoveryTestcaseCredConfig) throws CryptographyException {
        CredentialInfo discoveryTestcaseCredInfo = new CredentialInfoImpl();
        KeyPairInfo discoveryTestcaseKeyPairInfo;
        discoveryTestcaseCredInfo.setKeyPairDescriptor(discoveryTestcaseKeyPairInfo = KeyUtils.generateKeyPair(discoveryTestcaseCredConfig
            .getKeyPairDescriptor()));
        discoveryTestcaseCredInfo.setCertificateDescriptor(CertificateUtils.generateCertificate(discoveryTestcaseCaCredInfo, discoveryTestcaseKeyPairInfo,
            discoveryTestcaseCredConfig.getCertificateDescriptor()));

        return discoveryTestcaseCredInfo;
    }

    @Autowired
    @Override
    protected void setBeanDao(DiscoveryTestcaseCredentialDao beanDao) {
        super.setBeanDao(beanDao);
    }
}
