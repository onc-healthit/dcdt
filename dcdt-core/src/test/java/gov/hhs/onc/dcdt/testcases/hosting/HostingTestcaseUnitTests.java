package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.testcases.LocationType;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.unit.testcases.all", "dcdt.test.unit.testcases.hosting" })
public class HostingTestcaseUnitTests extends AbstractToolUnitTests {
    @Resource(name = "hostingTestcase1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HostingTestcase hostingTestcase1;

    @Resource(name = "hostingTestcaseResultPassedAnyBound")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HostingTestcaseResult hostingTestcaseResultPassedAnyBound;

    @Test
    public void testHostingTestcaseConfiguration() {
        Assert.assertNotNull(this.hostingTestcase1.getName());
        Assert.assertSame(this.hostingTestcase1.getBindingType(), BindingType.ADDRESS);
        Assert.assertSame(this.hostingTestcase1.getLocationType(), LocationType.DNS);

        HostingTestcaseDescription hostingTestcase1Desc = this.hostingTestcase1.getDescription();
        Assert.assertNotNull(hostingTestcase1Desc);
        Assert.assertEquals(CollectionUtils.size(hostingTestcase1Desc.getSpecifications()), 2);

        this.hostingTestcase1.setResult(this.hostingTestcaseResultPassedAnyBound);
        Assert.assertTrue(this.hostingTestcaseResultPassedAnyBound.isPassed());

        HostingTestcaseResult hostingTestcase1Result = this.hostingTestcase1.getResult();
        Assert.assertNotNull(hostingTestcase1Result);
        Assert.assertTrue(StringUtils.contains(hostingTestcase1Result.getMessage(), "This test case passed."));
    }
}
