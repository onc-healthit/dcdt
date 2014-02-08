package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigDao;
import gov.hhs.onc.dcdt.config.InstanceConfigService;
import gov.hhs.onc.dcdt.data.tx.services.impl.AbstractToolBeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("instanceConfigServiceImpl")
public class InstanceConfigServiceImpl extends AbstractToolBeanService<InstanceConfig, InstanceConfigDao> implements InstanceConfigService {
    @Autowired
    @Override
    protected void setBeanDao(InstanceConfigDao beanDao) {
        super.setBeanDao(beanDao);
    }
}
