package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.testcases.all", "dcdt.test.unit.testcases.hosting" })
public class HostingTestcaseUnitTests extends ToolTestNgUnitTests {

    @Test
    public void testGetHostingTestcaseProperties() {
        HostingTestcase hostingTestcase1 = (HostingTestcase) this.applicationContext.getBean("hostingTestcase1");
        Assert.assertNotNull(hostingTestcase1.getName());
        Assert.assertEquals(hostingTestcase1.getBinding(), HostingTestcaseBinding.ADDRESS);
        Assert.assertEquals(hostingTestcase1.getLocation(), HostingTestcaseLocation.DNS);
        Assert.assertEquals(hostingTestcase1.getSpecifications().size(), 2);

        HostingTestcaseResult hostingTestcaseResult = (HostingTestcaseResult) this.applicationContext.getBean("hostingTestcaseResultABPassed");
        hostingTestcase1.setResult(hostingTestcaseResult);
        Assert.assertTrue(hostingTestcaseResult.isPassed());
        Assert.assertTrue(hostingTestcase1.getResult().getMessage().contains("This test case passed."));

        HostingTestcase hostingTestcase3 = (HostingTestcase) this.applicationContext.getBean("hostingTestcase3");
        Assert.assertEquals(hostingTestcase3.getSpecifications().size(), 1);
    }
}
