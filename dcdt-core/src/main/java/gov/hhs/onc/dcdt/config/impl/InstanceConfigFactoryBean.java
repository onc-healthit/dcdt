package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolFactoryBean;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigService;
import org.springframework.beans.factory.annotation.Autowired;

public class InstanceConfigFactoryBean extends AbstractToolFactoryBean<InstanceConfig> {
    @Autowired
    private InstanceConfigService instanceConfigService;

    public InstanceConfigFactoryBean() {
        super(InstanceConfig.class);
    }

    @Override
    protected InstanceConfig createInstance() throws Exception {
        InstanceConfig instanceConfig = new InstanceConfigImpl(), instanceConfigExisting = this.instanceConfigService.getFirstBean();

        if (instanceConfigExisting != null) {
            instanceConfig.setDomain(instanceConfigExisting.getDomain());
            
            this.instanceConfigService.updateBean(instanceConfig);
        }
        
        return instanceConfig;
    }
}
