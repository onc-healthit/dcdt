package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.test.ToolTestNgFunctionalTests;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.func.config.all" }, groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.testcases.all",
    "dcdt.test.func.testcases.discovery" })
public class DiscoveryTestcaseFunctionalTests extends ToolTestNgFunctionalTests {
    private List<DiscoveryTestcase> discoveryTestcases;

    @Test(dependsOnMethods = { "testDiscoveryTestcaseConfigurations" })
    public void testDiscoveryTestcaseMailAddresses() {
        String instanceDomain = ToolBeanFactoryUtils.getBeanOfType(this.applicationContext.getBeanFactory(), InstanceConfig.class).getDomain(), discoveryTestcaseMailAddr;

        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            discoveryTestcaseMailAddr = discoveryTestcase.getMailAddress();

            Assert.assertTrue(StringUtils.endsWith(discoveryTestcaseMailAddr, instanceDomain), String.format(
                "Discovery testcase (name=%s) mail address (%s) does not end with instance domain (%s).", discoveryTestcase.getName(),
                discoveryTestcaseMailAddr, instanceDomain));
        }
    }

    @Test
    public void testDiscoveryTestcaseConfigurations() {
        for (DiscoveryTestcase discoveryTestcase : (this.discoveryTestcases = ToolBeanFactoryUtils.getBeansOfType(this.applicationContext.getBeanFactory(),
            DiscoveryTestcase.class))) {
            Assert.assertNotNull(discoveryTestcase.getName(), "Discovery testcase does not have a name.");
            Assert.assertNotNull(discoveryTestcase.getDescription(), "Discovery testcase does not have a description.");
        }
    }
}
