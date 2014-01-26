package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateGenerator;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.credentials.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyGenerator;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.data.registry.impl.AbstractToolBeanRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredentialService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDao;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseService;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseRegistryImpl")
@Scope("singleton")
public class DiscoveryTestcaseRegistryImpl extends AbstractToolBeanRegistry<DiscoveryTestcase, DiscoveryTestcaseDao, DiscoveryTestcaseService> implements
    DiscoveryTestcaseRegistry {
    private final static Logger LOGGER = LoggerFactory.getLogger(DiscoveryTestcaseRegistryImpl.class);

    @Autowired
    private KeyGenerator keyGen;

    @Autowired
    private CertificateGenerator certGen;

    private DiscoveryTestcaseCredential discoveryTestcaseIssuerCred;

    public DiscoveryTestcaseRegistryImpl() {
        super(DiscoveryTestcase.class, DiscoveryTestcaseService.class);
    }

    @Override
    @SuppressWarnings({ "ConstantConditions" })
    protected void registerBean(DiscoveryTestcase bean) throws ToolBeanRegistryException {
        if (bean.hasCredentials()) {
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
                                "Unable to generate Discovery testcase (name=%s, mailAddr=%s) issuer credential (name=%s).", bean.getName(),
                                bean.getMailAddress(), this.discoveryTestcaseIssuerCred.getName()), e);
                        }
                    }

                    discoveryTestcaseCred.setIssuerCredential(this.discoveryTestcaseIssuerCred);

                    if (!discoveryTestcaseCred.hasCredentialInfo()) {
                        try {
                            discoveryTestcaseCred.setCredentialInfo(this.generateDiscoveryTestcaseCredential(
                                this.discoveryTestcaseIssuerCred.getCredentialInfo(), discoveryTestcaseCred.getCredentialConfig()));

                            LOGGER.info(String.format("Generated Discovery testcase (name=%s, mailAddr=%s) credential (name=%s).", bean.getName(),
                                bean.getMailAddress(), discoveryTestcaseCred.getName()));
                        } catch (CryptographyException e) {
                            throw new ToolBeanRegistryException(String.format(
                                "Unable to generate Discovery testcase (name=%s, mailAddr=%s) credential (name=%s).", bean.getName(), bean.getMailAddress(),
                                discoveryTestcaseCred.getName()), e);
                        }
                    }
                }
            }
        }

        super.registerBean(bean);
    }

    @Override
    @SuppressWarnings({ "ConstantConditions" })
    protected void removeBean(DiscoveryTestcase bean) throws ToolBeanRegistryException {
        if ((this.discoveryTestcaseIssuerCred == null) && bean.hasCredentials()) {
            for (DiscoveryTestcaseCredential discoveryTestcaseCred : bean.getCredentials()) {
                if (discoveryTestcaseCred.hasIssuerCredential()) {
                    this.discoveryTestcaseIssuerCred = discoveryTestcaseCred.getIssuerCredential();
                }
            }
        }

        super.removeBean(bean);
    }

    @Override
    protected void preRegisterBeans(Iterable<DiscoveryTestcase> beans) throws ToolBeanRegistryException {
        this.discoveryTestcaseIssuerCred = null;
    }

    @Override
    protected void preRemoveBeans(Iterable<DiscoveryTestcase> beans) throws ToolBeanRegistryException {
        this.discoveryTestcaseIssuerCred = null;
    }

    @Override
    protected void postRemoveBeans(Iterable<DiscoveryTestcase> beans) throws ToolBeanRegistryException {
        if (this.discoveryTestcaseIssuerCred != null) {
            ToolBeanFactoryUtils.getBeanOfType(this.appContext, DiscoveryTestcaseCredentialService.class).removeBean(this.discoveryTestcaseIssuerCred);
        }
    }

    protected CredentialInfo generateDiscoveryTestcaseCredential(@Nullable CredentialInfo discoveryTestcaseIssuerCredInfo,
        CredentialConfig discoveryTestcaseCredConfig) throws CryptographyException {
        CredentialInfo discoveryTestcaseCredInfo = new CredentialInfoImpl();
        KeyInfo discoveryTestcaseKeyPairInfo;

        discoveryTestcaseCredInfo.setKeyDescriptor(discoveryTestcaseKeyPairInfo = this.keyGen.generateKeys(discoveryTestcaseCredConfig.getKeyDescriptor()));
        discoveryTestcaseCredInfo.setCertificateDescriptor(this.certGen.generateCertificate(discoveryTestcaseIssuerCredInfo, discoveryTestcaseKeyPairInfo,
            discoveryTestcaseCredConfig.getCertificateDescriptor()));

        return discoveryTestcaseCredInfo;
    }
}
