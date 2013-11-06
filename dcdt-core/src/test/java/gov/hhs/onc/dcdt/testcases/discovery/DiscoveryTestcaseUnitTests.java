package gov.hhs.onc.dcdt.testcases.discovery;


import gov.hhs.onc.dcdt.config.ToolInstance;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.config.all" }, groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.testcases.all",
    "dcdt.test.unit.testcases.discovery" })
public class DiscoveryTestcaseUnitTests extends ToolTestNgUnitTests {
    @Autowired(required = false)
    private ToolInstance instance;

    @Autowired(required = false)
    private List<DiscoveryTestcase> discoveryTestcases;

    @Test
    public void testAutowire() {
        Assert.assertFalse(ToolCollectionUtils.isEmpty(this.discoveryTestcases), "Unable to autowire Discovery testcase(s).");
    }

    @Test(dependsOnMethods = { "testAutowire" })
    public void testAutowireInstances() {
        String instanceDomain = this.instance.getDomain();
        String discoveryTestcaseMailAddr;

        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            discoveryTestcaseMailAddr = discoveryTestcase.getMailAddress();

            Assert.assertTrue(discoveryTestcaseMailAddr.endsWith(instanceDomain), "Discovery testcase (name=" + discoveryTestcase.getName()
                + ") mail address does not end with instance domain: " + discoveryTestcaseMailAddr);
        }
    }
}
