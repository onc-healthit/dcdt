package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.data.registry.impl.AbstractToolBeanRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDao;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseService;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseRegistryImpl")
@Scope("singleton")
public class DiscoveryTestcaseRegistryImpl extends AbstractToolBeanRegistry<DiscoveryTestcase, DiscoveryTestcaseDao, DiscoveryTestcaseService> implements
    DiscoveryTestcaseRegistry {
    public DiscoveryTestcaseRegistryImpl() {
        super(DiscoveryTestcase.class, DiscoveryTestcaseService.class);
    }

    @Override
    protected void preRemoveBeans(Iterable<DiscoveryTestcase> beans) throws ToolBeanRegistryException {
        this.getDiscoveryTestcaseCredentialRegistry().removeBeans(this.findDiscoveryTestcaseCredentials(beans));
    }

    @Override
    protected void postRegisterBeans(Iterable<DiscoveryTestcase> beans) throws ToolBeanRegistryException {
        this.getDiscoveryTestcaseCredentialRegistry().registerBeans(this.findDiscoveryTestcaseCredentials(beans));

        this.appContext.refresh();
    }

    @Override
    protected void postRemoveBeans(Iterable<DiscoveryTestcase> beans) throws ToolBeanRegistryException {
        this.appContext.refresh();
    }

    protected List<DiscoveryTestcaseCredential> findDiscoveryTestcaseCredentials(Iterable<DiscoveryTestcase> beans) {
        List<DiscoveryTestcaseCredential> discoveryTestcaseCreds = new ArrayList<>();

        for (DiscoveryTestcase bean : beans) {
            ToolCollectionUtils.addAll(discoveryTestcaseCreds, bean.getCredentials());
        }

        return discoveryTestcaseCreds;
    }

    protected DiscoveryTestcaseCredentialRegistry getDiscoveryTestcaseCredentialRegistry() {
        return ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), DiscoveryTestcaseCredentialRegistry.class);
    }
}
