package gov.hhs.onc.dcdt.testcases.discovery;


import gov.hhs.onc.dcdt.config.ToolInstance;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(/* dependsOnGroups = { "dcdt.test.config.all" }, */groups = { "dcdt.test.all", "dcdt.test.testcases.all", "dcdt.test.testcases.discovery" })
public class DiscoveryTestcaseUnitTests extends ToolTestNgUnitTests {
    @Autowired
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

            Assert.assertTrue(discoveryTestcaseMailAddr.endsWith(instanceDomain), "Discovery testcase (name=) mail address does not end with instance domain: "
                    + discoveryTestcaseMailAddr);
        }
    }
}
