package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.test.ToolTestNgFunctionalTests;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.func.config.all", "dcdt.test.func.data.tx.all" }, groups = { "dcdt.test.all", "dcdt.test.func.all",
    "dcdt.test.func.testcases.all", "dcdt.test.func.testcases.discovery" })
public class DiscoveryTestcaseFunctionalTests extends ToolTestNgFunctionalTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private InstanceConfig instanceConfig;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DiscoveryTestcaseService discoveryTestcaseService;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<DiscoveryTestcase> discoveryTestcases;

    @Test(dependsOnMethods = { "testDiscoveryTestcaseMailAddresses" })
    public void testProcessDiscoveryTestcases() throws CryptographyException {
        this.discoveryTestcaseService.processDiscoveryTestcases();
    }

    @Test(dependsOnMethods = { "testDiscoveryTestcaseConfigurations" })
    public void testDiscoveryTestcaseMailAddresses() {
        String instanceDomain = this.instanceConfig.getDomain(), discoveryTestcaseMailAddr;

        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            discoveryTestcaseMailAddr = discoveryTestcase.getMailAddress();

            Assert.assertTrue(StringUtils.endsWith(discoveryTestcaseMailAddr, instanceDomain), String.format(
                "Discovery testcase (name=%s) mail address (%s) does not end with instance domain (%s).", discoveryTestcase.getName(),
                discoveryTestcaseMailAddr, instanceDomain));
        }
    }

    @Test
    public void testDiscoveryTestcaseConfigurations() {
        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            Assert.assertNotNull(discoveryTestcase.getName(), "Discovery testcase does not have a name.");
            Assert.assertNotNull(discoveryTestcase.getDescription(), "Discovery testcase does not have a description.");
        }
    }
}
