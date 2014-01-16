package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolFactoryBean;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseService;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class DiscoveryTestcaseFactoryBean extends AbstractToolFactoryBean<DiscoveryTestcase> implements ApplicationContextAware {
    private AbstractApplicationContext appContext;
    private DiscoveryTestcase discoveryTestcase = new DiscoveryTestcaseImpl();

    public DiscoveryTestcaseFactoryBean() {
        super(DiscoveryTestcase.class);
    }

    @Override
    protected DiscoveryTestcase createInstance() throws Exception {
        ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), DiscoveryTestcaseService.class).loadBean(this.discoveryTestcase);

        return this.discoveryTestcase;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    public void setCredentials(List<DiscoveryTestcaseCredential> creds) {
        this.discoveryTestcase.setCredentials(creds);
    }

    public void setDescription(DiscoveryTestcaseDescription desc) {
        this.discoveryTestcase.setDescription(desc);
    }

    public void setMailAddress(String mailAddr) {
        this.discoveryTestcase.setMailAddress(mailAddr);
    }

    public void setName(String name) {
        this.discoveryTestcase.setName(name);
    }

    public void setNameDisplay(String nameDisplay) {
        this.discoveryTestcase.setNameDisplay(nameDisplay);
    }

    public void setNegative(boolean neg) {
        this.discoveryTestcase.setNegative(neg);
    }

    public void setOptional(boolean optional) {
        this.discoveryTestcase.setOptional(optional);
    }
}
