package gov.hhs.onc.dcdt.testcases.discovery.credentials.impl;

import gov.hhs.onc.dcdt.data.tx.services.impl.AbstractToolBeanService;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialDao;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("discoveryTestcaseCredServiceImpl")
public class DiscoveryTestcaseCredentialServiceImpl extends AbstractToolBeanService<DiscoveryTestcaseCredential, DiscoveryTestcaseCredentialDao> implements
    DiscoveryTestcaseCredentialService {
    @Autowired
    @Override
    protected void setBeanDao(DiscoveryTestcaseCredentialDao beanDao) {
        super.setBeanDao(beanDao);
    }
}
