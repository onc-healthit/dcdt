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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseCredRegistryImpl")
public class DiscoveryTestcaseCredentialRegistryImpl extends
    AbstractToolBeanRegistry<DiscoveryTestcaseCredential, DiscoveryTestcaseCredentialDao, DiscoveryTestcaseCredentialService> implements
    DiscoveryTestcaseCredentialRegistry {
    private final static Logger LOGGER = LoggerFactory.getLogger(DiscoveryTestcaseCredentialRegistryImpl.class);

    @Autowired
    private KeyGenerator keyGen;

    @Autowired
    private CertificateGenerator certGen;

    private Map<String, DiscoveryTestcaseCredential> discoveryTestcaseIssuerCredMap = new HashMap<>();

    public DiscoveryTestcaseCredentialRegistryImpl() {
        super(DiscoveryTestcaseCredential.class, DiscoveryTestcaseCredentialService.class);
    }

    @Override
    @SuppressWarnings({ "ConstantConditions" })
    protected void registerBean(DiscoveryTestcaseCredential bean) throws ToolBeanRegistryException {
        if (!bean.hasCredentialInfo()) {
            try {
                bean.setCredentialInfo(this.generateDiscoveryTestcaseCredential((bean.hasIssuerCredential()
                    ? bean.getIssuerCredential().getCredentialInfo() : null), bean.getCredentialConfig()));

                LOGGER.info(String.format("Generated Discovery testcase credential (name=%s).", bean.getName()));
            } catch (CryptographyException e) {
                throw new ToolBeanRegistryException(String.format("Unable to generate Discovery testcase credential (name=%s).", bean.getName()), e);
            }
        }

        super.registerBean(bean);
    }

    @Override
    protected void preRegisterBeans(List<DiscoveryTestcaseCredential> beans) throws ToolBeanRegistryException {
        this.discoveryTestcaseIssuerCredMap.clear();

        beans.stream().forEach(this::findDiscoveryTestcaseIssuerCredential);

        List<DiscoveryTestcaseCredential> discoveryTestcaseIssuerCreds = new ArrayList<>(this.discoveryTestcaseIssuerCredMap.values());
        discoveryTestcaseIssuerCreds.sort(OrderComparator.INSTANCE);

        for (DiscoveryTestcaseCredential discoveryTestcaseIssuerCred : discoveryTestcaseIssuerCreds) {
            if (!discoveryTestcaseIssuerCred.hasCredentialInfo()) {
                CredentialConfig discoveryTestcaseIssuerCredConfig = discoveryTestcaseIssuerCred.getCredentialConfig();

                try {
                    // noinspection ConstantConditions
                    discoveryTestcaseIssuerCred.setCredentialInfo(this.generateDiscoveryTestcaseCredential((discoveryTestcaseIssuerCred.hasIssuerCredential()
                        ? discoveryTestcaseIssuerCred.getIssuerCredential().getCredentialInfo() : null), discoveryTestcaseIssuerCredConfig));

                    LOGGER.info(String.format("Generated Discovery testcase issuer credential (name=%s).", discoveryTestcaseIssuerCred.getName()));
                } catch (CryptographyException e) {
                    throw new ToolBeanRegistryException(String.format("Unable to generate Discovery testcase issuer credential (name=%s).",
                        discoveryTestcaseIssuerCred.getName()), e);
                }

                this.registerBean(discoveryTestcaseIssuerCred);
            }
        }
    }

    @Override
    protected void preRemoveBeans(List<DiscoveryTestcaseCredential> beans) throws ToolBeanRegistryException {
        this.discoveryTestcaseIssuerCredMap.clear();

        beans.stream().forEach(this::findDiscoveryTestcaseIssuerCredential);
    }

    @Override
    protected void postRemoveBeans(List<DiscoveryTestcaseCredential> beans) throws ToolBeanRegistryException {
        if (this.discoveryTestcaseIssuerCredMap.isEmpty()) {
            return;
        }

        List<DiscoveryTestcaseCredential> discoveryTestcaseIssuerCreds = new ArrayList<>(this.discoveryTestcaseIssuerCredMap.values());
        discoveryTestcaseIssuerCreds.sort(OrderComparator.INSTANCE.reversed());

        this.getBeanService().removeBeans(discoveryTestcaseIssuerCreds);

        this.discoveryTestcaseIssuerCredMap.clear();
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

    protected void findDiscoveryTestcaseIssuerCredential(DiscoveryTestcaseCredential bean) {
        if (!bean.hasIssuerCredential()) {
            this.discoveryTestcaseIssuerCredMap.put(bean.getName(), bean);

            return;
        }

        DiscoveryTestcaseCredential discoveryTestcaseIssuerCred = bean.getIssuerCredential();
        // noinspection ConstantConditions
        String discoveryTestcaseIssuerCredName = discoveryTestcaseIssuerCred.getName();

        if (!this.discoveryTestcaseIssuerCredMap.containsKey(discoveryTestcaseIssuerCredName)) {
            this.discoveryTestcaseIssuerCredMap.put(discoveryTestcaseIssuerCredName, discoveryTestcaseIssuerCred);

            this.findDiscoveryTestcaseIssuerCredential(discoveryTestcaseIssuerCred);
        }
    }
}
