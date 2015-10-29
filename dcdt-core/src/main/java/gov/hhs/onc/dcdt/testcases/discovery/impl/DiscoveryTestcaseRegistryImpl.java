package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.data.registry.impl.AbstractToolBeanRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDao;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseService;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialRegistry;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseRegistryImpl")
public class DiscoveryTestcaseRegistryImpl extends AbstractToolBeanRegistry<DiscoveryTestcase, DiscoveryTestcaseDao, DiscoveryTestcaseService> implements
    DiscoveryTestcaseRegistry {
    public DiscoveryTestcaseRegistryImpl() {
        super(DiscoveryTestcase.class, DiscoveryTestcaseService.class);
    }

    @Override
    protected void preRemoveBeans(List<DiscoveryTestcase> beans) throws ToolBeanRegistryException {
        this.getDiscoveryTestcaseCredentialRegistry().removeBeans(this.extractDiscoveryTestcaseCredentials(beans));
    }

    @Override
    protected void postRegisterBeans(List<DiscoveryTestcase> beans) throws ToolBeanRegistryException {
        this.getDiscoveryTestcaseCredentialRegistry().registerBeans(this.extractDiscoveryTestcaseCredentials(beans));

        this.appContext.refresh();
    }

    @Override
    protected void postRemoveBeans(List<DiscoveryTestcase> beans) throws ToolBeanRegistryException {
        this.appContext.refresh();
    }

    protected List<DiscoveryTestcaseCredential> extractDiscoveryTestcaseCredentials(List<DiscoveryTestcase> beans) {
        // noinspection ConstantConditions
        return beans.stream().flatMap(bean -> (bean.hasCredentials() ? bean.getCredentials().stream() : Stream.<DiscoveryTestcaseCredential>empty()))
            .collect(Collectors.toList());
    }

    protected DiscoveryTestcaseCredentialRegistry getDiscoveryTestcaseCredentialRegistry() {
        return ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), DiscoveryTestcaseCredentialRegistry.class);
    }
}
