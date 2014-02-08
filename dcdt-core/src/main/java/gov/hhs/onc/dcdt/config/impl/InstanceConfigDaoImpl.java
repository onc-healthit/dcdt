package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigDao;
import gov.hhs.onc.dcdt.data.dao.impl.AbstractToolBeanDao;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

@DependsOn({ "instanceConfigDataSourceInit" })
@Repository("instanceConfigDaoImpl")
public class InstanceConfigDaoImpl extends AbstractToolBeanDao<InstanceConfig> implements InstanceConfigDao {
    public InstanceConfigDaoImpl() {
        super(InstanceConfig.class, InstanceConfigImpl.class);
    }
}
