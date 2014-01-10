package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.data.dao.impl.AbstractToolBeanDao;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("discoveryTestcaseDaoImpl")
@Scope("singleton")
public class DiscoveryTestcaseDaoImpl extends AbstractToolBeanDao<DiscoveryTestcase> implements DiscoveryTestcaseDao {
    public DiscoveryTestcaseDaoImpl() {
        super(DiscoveryTestcase.class);
    }
}
