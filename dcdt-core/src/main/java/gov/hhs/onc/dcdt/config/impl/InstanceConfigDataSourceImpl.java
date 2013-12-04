package gov.hhs.onc.dcdt.config.impl;


import gov.hhs.onc.dcdt.beans.impl.AbstractToolBeanDataSource;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigDataSource;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;

public class InstanceConfigDataSourceImpl extends AbstractToolBeanDataSource<InstanceConfig> implements InstanceConfigDataSource {
    @Autowired
    @Override
    protected void setBean(InstanceConfig bean) {
        super.setBean(bean);
    }
}
