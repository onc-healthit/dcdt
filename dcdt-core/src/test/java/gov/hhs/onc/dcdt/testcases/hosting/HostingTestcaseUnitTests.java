package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.unit.testcases.all", "dcdt.test.unit.testcases.hosting" })
public class HostingTestcaseUnitTests extends AbstractToolUnitTests {
    @Test
    public void testHostingTestcaseConfiguration() {
        String hostingTestcaseName;

        for (HostingTestcase hostingTestcase : ToolBeanFactoryUtils.getBeansOfType(this.applicationContext.getBeanFactory(), HostingTestcase.class)) {
            Assert.assertTrue(hostingTestcase.hasName(), String.format("Hosting testcase (name=%s) does not have a name.", hostingTestcaseName = hostingTestcase.getName()));
            Assert.assertTrue(hostingTestcase.hasDescription(), String.format("Hosting testcase (name=%s) does not have a description.", hostingTestcaseName));
            Assert.assertTrue(hostingTestcase.hasResult(), String.format("Hosting testcase (name=%s) does not have a result.", hostingTestcaseName));
        }
    }
}
