package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.mail.ToolMailAddressException;
import gov.hhs.onc.dcdt.test.ToolTestNgFunctionalTests;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xbill.DNS.Name;

@Test(dependsOnGroups = { "dcdt.test.func.config.all" }, groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.testcases.all",
    "dcdt.test.func.testcases.discovery" })
public class DiscoveryTestcaseFunctionalTests extends ToolTestNgFunctionalTests {
    private List<DiscoveryTestcase> discoveryTestcases;

    @Test(dependsOnMethods = { "testDiscoveryTestcaseConfigurations" })
    public void testDiscoveryTestcaseMailAddresses() throws ToolMailAddressException {
        Name instanceDomainName = ToolBeanFactoryUtils.getBeanOfType(this.applicationContext.getBeanFactory(), InstanceConfig.class).getDomainName(), discoveryTestcaseMailAddrDomainName;

        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            discoveryTestcaseMailAddrDomainName = discoveryTestcase.getMailAddress().getDomainName();

            Assert.assertTrue(discoveryTestcaseMailAddrDomainName.subdomain(instanceDomainName), String.format(
                "Discovery testcase (name=%s) mail address (%s) domain name is not a subdomain of the instance configuration domain name (%s).",
                discoveryTestcase.getName(), discoveryTestcaseMailAddrDomainName, instanceDomainName));
        }
    }

    @Test
    public void testDiscoveryTestcaseConfigurations() {
        for (DiscoveryTestcase discoveryTestcase : (this.discoveryTestcases =
            ToolBeanFactoryUtils.getBeansOfType(this.applicationContext.getBeanFactory(), DiscoveryTestcase.class))) {
            Assert.assertNotNull(discoveryTestcase.getName(), "Discovery testcase does not have a name.");
            Assert.assertNotNull(discoveryTestcase.getDescription(), "Discovery testcase does not have a description.");
        }
    }
}
