package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigDao;
import gov.hhs.onc.dcdt.data.dao.impl.AbstractToolBeanDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("instanceConfigDaoImpl")
@Scope("singleton")
public class InstanceConfigDaoImpl extends AbstractToolBeanDao<InstanceConfig> implements InstanceConfigDao {
    public InstanceConfigDaoImpl() {
        super(InstanceConfig.class, InstanceConfigImpl.class);
    }
}
