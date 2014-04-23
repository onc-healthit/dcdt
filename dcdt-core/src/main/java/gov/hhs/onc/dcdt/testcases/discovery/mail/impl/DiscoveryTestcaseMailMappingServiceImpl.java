package gov.hhs.onc.dcdt.testcases.discovery.mail.impl;

import gov.hhs.onc.dcdt.data.tx.services.impl.AbstractToolBeanService;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingDao;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("discoveryTestcaseMailMappingServiceImpl")
public class DiscoveryTestcaseMailMappingServiceImpl extends AbstractToolBeanService<DiscoveryTestcaseMailMapping, DiscoveryTestcaseMailMappingDao> implements
    DiscoveryTestcaseMailMappingService {
    @Autowired
    @Override
    protected void setBeanDao(DiscoveryTestcaseMailMappingDao beanDao) {
        super.setBeanDao(beanDao);
    }
}
