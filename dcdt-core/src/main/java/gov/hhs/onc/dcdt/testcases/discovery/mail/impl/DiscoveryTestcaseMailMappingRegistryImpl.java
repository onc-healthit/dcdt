package gov.hhs.onc.dcdt.testcases.discovery.mail.impl;

import gov.hhs.onc.dcdt.data.registry.impl.AbstractToolBeanRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingDao;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingService;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseMailMappingRegistryImpl")
public class DiscoveryTestcaseMailMappingRegistryImpl extends
    AbstractToolBeanRegistry<DiscoveryTestcaseMailMapping, DiscoveryTestcaseMailMappingDao, DiscoveryTestcaseMailMappingService> implements
    DiscoveryTestcaseMailMappingRegistry {
    public DiscoveryTestcaseMailMappingRegistryImpl() {
        super(DiscoveryTestcaseMailMapping.class, DiscoveryTestcaseMailMappingService.class);
    }
}
