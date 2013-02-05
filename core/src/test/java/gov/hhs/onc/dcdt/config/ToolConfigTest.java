package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.test.config.ToolTestConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.reflect.resources" }, groups = { "dcdt.all", "dcdt.config", "dcdt.config.all" })
public class ToolConfigTest
{
	/*private*/ static ToolConfig config;
	
	@BeforeGroups({ "dcdt.config" })
	public static void setUp() throws ToolConfigException
	{
		config = ToolTestConfig.getInstance();
	}
	
	@Test
	public void testGetChildConfig()
	{
		Assert.assertNotNull(config.getChildPropsConfig("test-props"), 
			"Failed to get child properties configuration.");
		Assert.assertNotNull(config.getChildXmlConfig("test-xml"), 
			"Failed to get child XML configuration.");
	}
}