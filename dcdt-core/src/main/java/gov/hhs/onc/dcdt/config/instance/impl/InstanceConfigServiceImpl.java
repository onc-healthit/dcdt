package gov.hhs.onc.dcdt.config.instance.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigDao;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigService;
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
