package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.config.impl.InstanceConfigImpl;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.test.ToolTestNgFunctionalTests;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.config.all", "dcdt.test.func.config.instance" })
public class InstanceConfigRegistryFunctionalTests extends ToolTestNgFunctionalTests {
    @Value("${dcdt.test.instance.domain.base}")
    private String testInstanceConfigDomainBase;

    @Value("${dcdt.test.instance.domain.rm}")
    private String testInstanceConfigDomainRm;

    @Test(dependsOnMethods = { "testRemoveBean" })
    public void testRegisterBean() throws ToolBeanRegistryException {
        InstanceConfig instanceConfig = this.getInstanceConfig();
        instanceConfig.setDomain(this.testInstanceConfigDomainBase);

        this.getInstanceConfigRegistry().registerBeans(instanceConfig);

        Assert.assertEquals(this.getInstanceConfig().getDomain(), this.testInstanceConfigDomainBase, "Instance configuration domains are not equal.");
    }

    @Test
    public void testRemoveBean() throws ToolBeanRegistryException {
        InstanceConfig instanceConfig = new InstanceConfigImpl();
        instanceConfig.setDomain(this.testInstanceConfigDomainRm);

        this.getInstanceConfigService().setBean(instanceConfig);
        this.getInstanceConfigRegistry().removeBeans(instanceConfig);

        Assert.assertNotEquals(this.getInstanceConfig().getDomain(), this.testInstanceConfigDomainRm, "Instance configuration domains are equal.");
    }

    private InstanceConfigRegistry getInstanceConfigRegistry() {
        return ToolBeanFactoryUtils.getBeanOfType(this.applicationContext.getBeanFactory(), InstanceConfigRegistry.class);
    }

    private InstanceConfigService getInstanceConfigService() {
        return ToolBeanFactoryUtils.getBeanOfType(this.applicationContext.getBeanFactory(), InstanceConfigService.class);
    }

    private InstanceConfig getInstanceConfig() {
        return ToolBeanFactoryUtils.getBeanOfType(this.applicationContext.getBeanFactory(), InstanceConfig.class);
    }
}
