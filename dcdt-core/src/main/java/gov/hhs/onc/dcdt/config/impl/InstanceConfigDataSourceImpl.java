package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.data.impl.AbstractToolBeanDataSource;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigDataSource;
import org.springframework.beans.factory.annotation.Autowired;

public class InstanceConfigDataSourceImpl extends AbstractToolBeanDataSource<InstanceConfig> implements InstanceConfigDataSource {
    @Autowired
    @Override
    protected void setBean(InstanceConfig bean) {
        super.setBean(bean);
    }
}
