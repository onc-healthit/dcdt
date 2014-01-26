package gov.hhs.onc.dcdt.testcases.discovery.credentials.impl;

import gov.hhs.onc.dcdt.data.dao.impl.AbstractToolBeanDao;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialDao;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@DependsOn({ "instanceConfigDataSourceInit" })
@Repository("discoveryTestcaseCredDaoImpl")
@Scope("singleton")
public class DiscoveryTestcaseCredentialDaoImpl extends AbstractToolBeanDao<DiscoveryTestcaseCredential> implements DiscoveryTestcaseCredentialDao {
    public DiscoveryTestcaseCredentialDaoImpl() {
        super(DiscoveryTestcaseCredential.class, DiscoveryTestcaseCredentialImpl.class);
    }
}
