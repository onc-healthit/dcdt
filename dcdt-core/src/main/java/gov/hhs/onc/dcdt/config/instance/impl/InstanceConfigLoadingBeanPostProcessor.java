package gov.hhs.onc.dcdt.config.instance.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigDao;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigService;
import gov.hhs.onc.dcdt.data.impl.AbstractLoadingBeanPostProcessor;
import javax.annotation.Nullable;
import org.springframework.stereotype.Component;

@Component("beanLoadingPostProcInstanceConfig")
public class InstanceConfigLoadingBeanPostProcessor extends AbstractLoadingBeanPostProcessor<InstanceConfig, InstanceConfigDao, InstanceConfigService> {
    public InstanceConfigLoadingBeanPostProcessor() {
        // noinspection ConstantConditions
        super(InstanceConfig.class, InstanceConfigService.class);
    }

    @Override
    protected InstanceConfig loadBean(InstanceConfig bean, InstanceConfig persistentBean) throws Exception {
        bean.setDomainName(persistentBean.getDomainName());
        bean.setIpAddress(persistentBean.getIpAddress());

        return super.loadBean(bean, persistentBean);
    }

    @Nullable
    @Override
    protected InstanceConfig findPersistentBean(InstanceConfig bean, InstanceConfigService beanService) throws Exception {
        return beanService.getBean();
    }
}
