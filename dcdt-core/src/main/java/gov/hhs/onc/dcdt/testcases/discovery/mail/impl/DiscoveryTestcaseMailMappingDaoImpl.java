package gov.hhs.onc.dcdt.testcases.discovery.mail.impl;

import gov.hhs.onc.dcdt.data.dao.impl.AbstractToolBeanDao;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingDao;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

@DependsOn({ "instanceConfigDataSourceInit" })
@Repository("discoveryTestcaseMailMappingDaoImpl")
public class DiscoveryTestcaseMailMappingDaoImpl extends AbstractToolBeanDao<DiscoveryTestcaseMailMapping> implements DiscoveryTestcaseMailMappingDao {
    public DiscoveryTestcaseMailMappingDaoImpl() {
        super(DiscoveryTestcaseMailMapping.class, DiscoveryTestcaseMailMappingImpl.class);
    }
}
