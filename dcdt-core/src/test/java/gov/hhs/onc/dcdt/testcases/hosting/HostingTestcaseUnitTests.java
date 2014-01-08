package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import javax.annotation.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.testcases.all", "dcdt.test.unit.testcases.hosting" })
public class HostingTestcaseUnitTests extends ToolTestNgUnitTests {
    @Resource(name = "hostingTestcase1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HostingTestcase hostingTestcase1;

    @Resource(name = "hostingTestcaseResultPassedAnyBound")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HostingTestcaseResult hostingTestcaseResultPassedAnyBound;

    @Test
    public void testHostingTestcaseConfiguration() {
        Assert.assertNotNull(this.hostingTestcase1.getName());
        Assert.assertEquals(this.hostingTestcase1.getBinding(), HostingTestcaseBinding.ADDRESS);
        Assert.assertEquals(this.hostingTestcase1.getLocation(), HostingTestcaseLocation.DNS);
        Assert.assertEquals(this.hostingTestcase1.getDescription().getSpecifications().size(), 2);

        this.hostingTestcase1.setResult(this.hostingTestcaseResultPassedAnyBound);
        Assert.assertTrue(this.hostingTestcaseResultPassedAnyBound.isPassed());
        Assert.assertTrue(this.hostingTestcase1.getResult().getMessage().contains("This test case passed."));
    }
}
