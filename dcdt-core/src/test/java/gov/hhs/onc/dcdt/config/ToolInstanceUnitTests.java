package gov.hhs.onc.dcdt.config;


import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.config.all", "dcdt.test.config.instance" })
public class ToolInstanceUnitTests extends ToolTestNgUnitTests {
    @Autowired(required = false)
    private ToolInstance instance;

    @Test
    public void testAutowire() {
        Assert.assertNotNull(this.instance, "Unable to autowire instance configuration.");
        Assert.assertNotNull(this.instance.getDomain(), "Instance domain not configured.");
    }
}
