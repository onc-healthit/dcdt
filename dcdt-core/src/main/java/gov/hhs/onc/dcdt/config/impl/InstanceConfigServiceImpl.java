package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigDao;
import gov.hhs.onc.dcdt.config.InstanceConfigService;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.data.tx.services.impl.AbstractToolBeanService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseService;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("instanceConfigServiceImpl")
public class InstanceConfigServiceImpl extends AbstractToolBeanService<InstanceConfig, InstanceConfigDao> implements InstanceConfigService {
    @Autowired
    private DiscoveryTestcaseService discoveryTestcaseService;

    @Autowired
    private InstanceConfig instanceConfig;

    @Override
    public InstanceConfig processInstanceConfig() throws CryptographyException {
        this.instanceConfig = this.setBean(this.instanceConfig);

        this.appContext.refresh();

        this.instanceConfig = ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), InstanceConfig.class);
        (this.discoveryTestcaseService = ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), DiscoveryTestcaseService.class))
            .processDiscoveryTestcases();
        
        this.appContext.refresh();

        return this.instanceConfig;
    }

    @Autowired
    @Override
    protected void setBeanDao(InstanceConfigDao beanDao) {
        super.setBeanDao(beanDao);
    }
}
