package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.crypto.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.KeyUtils;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.data.registry.impl.AbstractToolBeanRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDao;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseService;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseRegistryImpl")
@Scope("singleton")
public class DiscoveryTestcaseRegistryImpl extends AbstractToolBeanRegistry<DiscoveryTestcase, DiscoveryTestcaseDao, DiscoveryTestcaseService> implements
    DiscoveryTestcaseRegistry {
    private final static Logger LOGGER = LoggerFactory.getLogger(DiscoveryTestcaseRegistryImpl.class);

    private DiscoveryTestcaseCredential discoveryTestcaseIssuerCred;

    public DiscoveryTestcaseRegistryImpl() {
        super(DiscoveryTestcase.class, DiscoveryTestcaseService.class);
    }

    @Override
    public void registerBean(DiscoveryTestcase bean) throws ToolBeanRegistryException {
        if (!bean.hasInstanceConfig()) {
            bean.setInstanceConfig(ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), InstanceConfig.class));
        }

        for (DiscoveryTestcaseCredential discoveryTestcaseCred : bean.getCredentials()) {
            if (!discoveryTestcaseCred.hasCredentialInfo()) {
                if (this.discoveryTestcaseIssuerCred == null) {
                    this.discoveryTestcaseIssuerCred = discoveryTestcaseCred.getIssuerCredential();
                }

                if (!this.discoveryTestcaseIssuerCred.hasCredentialInfo()) {
                    try {
                        this.discoveryTestcaseIssuerCred.setCredentialInfo(this.generateDiscoveryTestcaseCredential(null,
                            this.discoveryTestcaseIssuerCred.getCredentialConfig()));

                        LOGGER.info(String.format("Generated Discovery testcase (name=%s, mailAddr=%s) issuer credential (name=%s).", bean.getName(),
                            bean.getMailAddress(), this.discoveryTestcaseIssuerCred.getName()));
                    } catch (CryptographyException e) {
                        throw new ToolBeanRegistryException(String.format(
                            "Unable to generate Discovery testcase (name=%s, mailAddr=%s) issuer credential (name=%s).", bean.getName(), bean.getMailAddress(),
                            this.discoveryTestcaseIssuerCred.getName()), e);
                    }
                }

                discoveryTestcaseCred.setIssuerCredential(this.discoveryTestcaseIssuerCred);

                if (!discoveryTestcaseCred.hasCredentialInfo()) {
                    try {
                        discoveryTestcaseCred.setCredentialInfo(this.generateDiscoveryTestcaseCredential(this.discoveryTestcaseIssuerCred.getCredentialInfo(),
                            discoveryTestcaseCred.getCredentialConfig()));

                        LOGGER.info(String.format("Generated Discovery testcase (name=%s, mailAddr=%s) credential (name=%s).", bean.getName(),
                            bean.getMailAddress(), discoveryTestcaseCred.getName()));
                    } catch (CryptographyException e) {
                        throw new ToolBeanRegistryException(String.format("Unable to generate Discovery testcase (name=%s, mailAddr=%s) credential (name=%s).",
                            bean.getName(), bean.getMailAddress(), discoveryTestcaseCred.getName()), e);
                    }
                }
            }
        }

        super.registerBean(bean);
    }

    protected CredentialInfo generateDiscoveryTestcaseCredential(@Nullable CredentialInfo discoveryTestcaseIssuerCredInfo,
        CredentialConfig discoveryTestcaseCredConfig) throws CryptographyException {
        CredentialInfo discoveryTestcaseCredInfo = new CredentialInfoImpl();

        KeyPairInfo discoveryTestcaseKeyPairInfo;
        discoveryTestcaseCredInfo.setKeyPairDescriptor(discoveryTestcaseKeyPairInfo = KeyUtils.generateKeyPair(discoveryTestcaseCredConfig
            .getKeyPairDescriptor()));

        discoveryTestcaseCredInfo.setCertificateDescriptor(CertificateUtils.generateCertificate(discoveryTestcaseIssuerCredInfo, discoveryTestcaseKeyPairInfo,
            discoveryTestcaseCredConfig.getCertificateDescriptor()));

        return discoveryTestcaseCredInfo;
    }
}
