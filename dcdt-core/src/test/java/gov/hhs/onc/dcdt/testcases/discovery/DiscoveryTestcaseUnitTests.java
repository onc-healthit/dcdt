package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.config.all" }, groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.testcases.all",
    "dcdt.test.unit.testcases.discovery" })
public class DiscoveryTestcaseUnitTests extends ToolTestNgUnitTests {
    @Value("@{dcdt.instance.domain}")
    private String instanceDomain;
    
    @Autowired(required = false)
    private List<DiscoveryTestcase> discoveryTestcases;

    @Test
    public void testAutowire() {
        Assert.assertFalse(ToolCollectionUtils.isEmpty(this.discoveryTestcases), "Unable to autowire Discovery testcase(s).");
    }

    @Test(dependsOnMethods = { "testAutowire" })
    public void testAutowireInstances() {
        String discoveryTestcaseMailAddr;

        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            discoveryTestcaseMailAddr = discoveryTestcase.getMailAddress();

            Assert.assertTrue(discoveryTestcaseMailAddr.endsWith(instanceDomain), String.format(
                "Discovery testcase (name=%s) mail address (%s) does not end with instance domain (%s).", discoveryTestcase.getName(),
                discoveryTestcaseMailAddr, instanceDomain));
        }
    }
}
