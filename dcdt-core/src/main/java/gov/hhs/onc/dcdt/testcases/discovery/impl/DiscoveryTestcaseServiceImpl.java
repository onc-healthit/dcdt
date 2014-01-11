package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.data.tx.services.impl.AbstractToolBeanService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDao;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("discoveryTestcaseServiceImpl")
public class DiscoveryTestcaseServiceImpl extends AbstractToolBeanService<DiscoveryTestcase, DiscoveryTestcaseDao> implements DiscoveryTestcaseService {
    @Autowired
    @Override
    protected void setBeanDao(DiscoveryTestcaseDao beanDao) {
        super.setBeanDao(beanDao);
    }
}
