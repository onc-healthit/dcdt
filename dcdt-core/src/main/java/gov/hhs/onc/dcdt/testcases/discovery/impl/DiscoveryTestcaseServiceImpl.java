package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.crypto.CertificateAuthority;
import gov.hhs.onc.dcdt.crypto.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.data.tx.services.impl.AbstractToolBeanService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredentialService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDao;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("discoveryTestcaseServiceImpl")
public class DiscoveryTestcaseServiceImpl extends AbstractToolBeanService<DiscoveryTestcase, DiscoveryTestcaseDao> implements DiscoveryTestcaseService {
    @Autowired
    private DiscoveryTestcaseCredentialService discoveryTestcaseCredService;

    @Autowired
    @CertificateAuthority
    private DiscoveryTestcaseCredential discoveryTestcaseCaCred;

    @Autowired
    private List<DiscoveryTestcase> discoveryTestcases;

    @Override
    public List<DiscoveryTestcase> processDiscoveryTestcases() throws CryptographyException {
        CredentialInfo discoveryTestcaseCaCredInfo = (this.discoveryTestcaseCaCred = this.discoveryTestcaseCredService.processDiscoveryTestcaseCredential(
            null,
            (this.discoveryTestcaseCaCred = this.appContext.getBeanFactory().getBean(this.discoveryTestcaseCaCred.getBeanName(),
                DiscoveryTestcaseCredential.class)))).getCredentialInfo();

        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            for (DiscoveryTestcaseCredential discoveryTestcaseCred : discoveryTestcase.getCredentials()) {
                this.discoveryTestcaseCredService.processDiscoveryTestcaseCredential(discoveryTestcaseCaCredInfo, discoveryTestcaseCred);
            }

            this.setBean(discoveryTestcase);
        }

        return this.discoveryTestcases;
    }

    @Autowired
    @Override
    protected void setBeanDao(DiscoveryTestcaseDao beanDao) {
        super.setBeanDao(beanDao);
    }
}
