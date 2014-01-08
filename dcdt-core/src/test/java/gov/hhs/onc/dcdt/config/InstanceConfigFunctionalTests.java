package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.test.ToolTestNgFunctionalTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.config.all", "dcdt.test.func.config.instance" })
public class InstanceConfigFunctionalTests extends ToolTestNgFunctionalTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private InstanceConfigService instanceConfigService;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private InstanceConfig instanceConfig;

    @Value("${dcdt.test.instance.domain}")
    private String testInstanceConfigDomain;

    @Test
    public void testProcessInstanceConfig() throws CryptographyException {
        this.instanceConfig.setDomain(this.testInstanceConfigDomain);

        this.instanceConfig = this.instanceConfigService.processInstanceConfig();

        Assert.assertEquals(this.instanceConfig.getDomain(), this.testInstanceConfigDomain, "Instance configuration domains are not equal.");
    }
}
