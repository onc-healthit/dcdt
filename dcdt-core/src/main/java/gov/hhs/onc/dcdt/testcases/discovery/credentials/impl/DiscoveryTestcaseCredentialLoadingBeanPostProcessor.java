package gov.hhs.onc.dcdt.testcases.discovery.credentials.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.credentials.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.crypto.keys.impl.KeyInfoImpl;
import gov.hhs.onc.dcdt.data.impl.AbstractLoadingBeanPostProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialDao;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialService;
import javax.annotation.Nullable;
import org.springframework.stereotype.Component;

@Component("beanLoadingPostProcDiscoveryTestcaseCred")
public class DiscoveryTestcaseCredentialLoadingBeanPostProcessor extends
    AbstractLoadingBeanPostProcessor<DiscoveryTestcaseCredential, DiscoveryTestcaseCredentialDao, DiscoveryTestcaseCredentialService> {
    public DiscoveryTestcaseCredentialLoadingBeanPostProcessor() {
        super(DiscoveryTestcaseCredential.class, DiscoveryTestcaseCredentialService.class);
    }

    @Override
    protected DiscoveryTestcaseCredential loadBean(DiscoveryTestcaseCredential bean, DiscoveryTestcaseCredential persistentBean) throws Exception {
        CredentialInfo persistentDiscoveryTestcaseCredInfo;
        KeyInfo persistentDiscoveryTestcaseCredKeyInfo;
        CertificateInfo persistentDiscoveryTestcaseCredCertInfo;

        // noinspection ConstantConditions
        if (!persistentBean.hasCredentialInfo() || !(persistentDiscoveryTestcaseCredInfo = persistentBean.getCredentialInfo()).hasKeyDescriptor()
            || !(persistentDiscoveryTestcaseCredKeyInfo = persistentDiscoveryTestcaseCredInfo.getKeyDescriptor()).hasPrivateKey()
            || !persistentDiscoveryTestcaseCredInfo.hasCertificateDescriptor()
            || !(persistentDiscoveryTestcaseCredCertInfo = persistentDiscoveryTestcaseCredInfo.getCertificateDescriptor()).hasCertificate()) {
            return super.loadBean(bean, persistentBean);
        }

        // noinspection ConstantConditions
        bean.setCredentialInfo(new CredentialInfoImpl(new KeyInfoImpl(persistentDiscoveryTestcaseCredKeyInfo.getKeyPair()), new CertificateInfoImpl(
            persistentDiscoveryTestcaseCredCertInfo.getCertificate())));

        return super.loadBean(bean, persistentBean);
    }

    @Nullable
    @Override
    protected DiscoveryTestcaseCredential findPersistentBean(DiscoveryTestcaseCredential bean, DiscoveryTestcaseCredentialService beanService) throws Exception {
        return (bean.hasName() ? beanService.getBeanById(bean.getName()) : null);
    }
}
