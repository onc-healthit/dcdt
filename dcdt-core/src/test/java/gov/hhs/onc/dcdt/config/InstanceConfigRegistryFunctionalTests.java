package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.config.impl.InstanceConfigImpl;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.test.ToolTestNgFunctionalTests;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import java.net.InetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xbill.DNS.Name;

@Test(groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.config.all", "dcdt.test.func.config.instance" })
public class InstanceConfigRegistryFunctionalTests extends ToolTestNgFunctionalTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private InstanceConfigRegistry instanceConfigReg;

    @Value("${dcdt.test.instance.domain.name}")
    private Name testInstanceConfigDomainName;

    @Value("${dcdt.test.instance.domain.rm.name}")
    private Name testInstanceConfigDomainRmName;

    @Value("${dcdt.test.instance.ip.addr}")
    private InetAddress testInstanceConfigIpAddr;

    @Test(dependsOnMethods = { "testRemoveBeans" })
    public void testRegisterBeans() throws ToolBeanRegistryException {
        InstanceConfig instanceConfigRegister = this.getInstanceConfig();
        instanceConfigRegister.setDomainName(this.testInstanceConfigDomainName);
        instanceConfigRegister.setIpAddress(this.testInstanceConfigIpAddr);

        this.instanceConfigReg.registerBeans(instanceConfigRegister);

        InstanceConfig instanceConfig = this.getInstanceConfig();
        Assert.assertEquals(instanceConfig.getDomainName(), this.testInstanceConfigDomainName, "Instance configuration domain names are not equal.");
        Assert.assertEquals(instanceConfig.getIpAddress(), this.testInstanceConfigIpAddr, "Instance configuration IP addresses are not equal.");
    }

    @Test
    public void testRemoveBeans() throws ToolBeanRegistryException {
        InstanceConfig instanceConfigRm = new InstanceConfigImpl();
        instanceConfigRm.setDomainName(this.testInstanceConfigDomainRmName);
        instanceConfigRm.setIpAddress(this.testInstanceConfigIpAddr);

        this.getInstanceConfigService().setBean(instanceConfigRm);
        this.instanceConfigReg.removeBeans(instanceConfigRm);

        InstanceConfig instanceConfig = this.getInstanceConfig();
        Assert.assertNotEquals(instanceConfig.getDomainName(), this.testInstanceConfigDomainRmName, "Instance configuration domain names are equal.");
        Assert.assertNotEquals(instanceConfig.getIpAddress(), this.testInstanceConfigIpAddr, "Instance configuration IP addresses are equal.");
    }

    private InstanceConfigService getInstanceConfigService() {
        return ToolBeanFactoryUtils.getBeanOfType(this.applicationContext.getBeanFactory(), InstanceConfigService.class);
    }

    private InstanceConfig getInstanceConfig() {
        return ToolBeanFactoryUtils.getBeanOfType(this.applicationContext.getBeanFactory(), InstanceConfig.class);
    }
}
