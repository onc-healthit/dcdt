package gov.hhs.onc.dcdt.testcases.discovery.credentials.impl;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateGenerator;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.credentials.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyGenerator;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.data.registry.impl.AbstractToolBeanRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialDao;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialService;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseCredRegistryImpl")
@Scope("singleton")
public class DiscoveryTestcaseCredentialRegistryImpl extends
    AbstractToolBeanRegistry<DiscoveryTestcaseCredential, DiscoveryTestcaseCredentialDao, DiscoveryTestcaseCredentialService> implements
    DiscoveryTestcaseCredentialRegistry {
    private final static Logger LOGGER = LoggerFactory.getLogger(DiscoveryTestcaseCredentialRegistryImpl.class);

    @Autowired
    private KeyGenerator keyGen;

    @Autowired
    private CertificateGenerator certGen;

    private DiscoveryTestcaseCredential discoveryTestcaseIssuerCred;

    public DiscoveryTestcaseCredentialRegistryImpl() {
        super(DiscoveryTestcaseCredential.class, DiscoveryTestcaseCredentialService.class);
    }

    @Override
    @SuppressWarnings({ "ConstantConditions" })
    protected void registerBean(DiscoveryTestcaseCredential bean) throws ToolBeanRegistryException {
        if (!bean.hasCredentialInfo()) {
            bean.setIssuerCredential(this.discoveryTestcaseIssuerCred);

            if (!bean.hasCredentialInfo()) {
                try {
                    bean.setCredentialInfo(this.generateDiscoveryTestcaseCredential(this.discoveryTestcaseIssuerCred.getCredentialInfo(),
                        bean.getCredentialConfig()));

                    LOGGER.info(String.format("Generated Discovery testcase credential (name=%s).", bean.getName()));
                } catch (CryptographyException e) {
                    throw new ToolBeanRegistryException(String.format("Unable to generate Discovery testcase credential (name=%s).", bean.getName()), e);
                }
            }
        }

        super.registerBean(bean);
    }

    @Override
    protected void preRegisterBeans(Iterable<DiscoveryTestcaseCredential> beans) throws ToolBeanRegistryException {
        this.findDiscoveryTestcaseIssuerCredential(beans);

        if (!this.discoveryTestcaseIssuerCred.hasCredentialInfo()) {
            try {
                this.discoveryTestcaseIssuerCred.setCredentialInfo(this.generateDiscoveryTestcaseCredential(null,
                    this.discoveryTestcaseIssuerCred.getCredentialConfig()));

                LOGGER.info(String.format("Generated Discovery testcase issuer credential (name=%s).", this.discoveryTestcaseIssuerCred.getName()));
            } catch (CryptographyException e) {
                throw new ToolBeanRegistryException(String.format("Unable to generate Discovery testcase issuer credential (name=%s).",
                    this.discoveryTestcaseIssuerCred.getName()), e);
            }

            this.registerBean(this.discoveryTestcaseIssuerCred);
        }
    }

    @Override
    protected void preRemoveBeans(Iterable<DiscoveryTestcaseCredential> beans) throws ToolBeanRegistryException {
        this.findDiscoveryTestcaseIssuerCredential(beans);
    }

    @Override
    protected void postRemoveBeans(Iterable<DiscoveryTestcaseCredential> beans) throws ToolBeanRegistryException {
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

    protected void findDiscoveryTestcaseIssuerCredential(Iterable<DiscoveryTestcaseCredential> beans) {
        this.discoveryTestcaseIssuerCred = null;

        for (DiscoveryTestcaseCredential bean : beans) {
            if ((this.discoveryTestcaseIssuerCred == null) && bean.hasIssuerCredential()) {
                this.discoveryTestcaseIssuerCred = bean.getIssuerCredential();

                return;
            }
        }
    }
}
