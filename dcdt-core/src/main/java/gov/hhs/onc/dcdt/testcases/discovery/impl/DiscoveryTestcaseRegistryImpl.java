package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.data.registry.impl.AbstractToolBeanRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase.DiscoveryTestcaseCredentialsExtractor;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDao;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseService;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialRegistry;
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
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

    protected List<DiscoveryTestcaseCredential> extractDiscoveryTestcaseCredentials(Iterable<DiscoveryTestcase> beans) {
        return IteratorUtils.toList(ToolIteratorUtils.chainedIterator(CollectionUtils.collect(beans, DiscoveryTestcaseCredentialsExtractor.INSTANCE)));
    }

    protected DiscoveryTestcaseCredentialRegistry getDiscoveryTestcaseCredentialRegistry() {
        return ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), DiscoveryTestcaseCredentialRegistry.class);
    }
}
