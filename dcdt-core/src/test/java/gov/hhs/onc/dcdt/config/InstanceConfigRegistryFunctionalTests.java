package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.test.ToolTestNgFunctionalTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.config.all", "dcdt.test.func.config.instance" })
public class InstanceConfigRegistryFunctionalTests extends ToolTestNgFunctionalTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private InstanceConfigRegistry instanceConfigRegistry;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private InstanceConfig instanceConfig;

    @Value("${dcdt.test.instance.domain}")
    private String testInstanceConfigDomain;

    @Test
    public void testProcessBeans() throws ToolBeanRegistryException {
        this.instanceConfig.setDomain(this.testInstanceConfigDomain);

        this.instanceConfigRegistry.processBeans();

        Assert.assertEquals(this.instanceConfig.getDomain(), this.testInstanceConfigDomain, "Instance configuration domains are not equal.");
    }
}
