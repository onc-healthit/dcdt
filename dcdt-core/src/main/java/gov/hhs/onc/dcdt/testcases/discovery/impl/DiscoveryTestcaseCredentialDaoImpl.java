package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.data.dao.impl.AbstractToolBeanDao;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredentialDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("discoveryTestcaseCredDaoImpl")
@Scope("singleton")
public class DiscoveryTestcaseCredentialDaoImpl extends AbstractToolBeanDao<DiscoveryTestcaseCredential> implements DiscoveryTestcaseCredentialDao {
    public DiscoveryTestcaseCredentialDaoImpl() {
        super(DiscoveryTestcaseCredential.class);
    }
}
