package gov.hhs.onc.dcdt.reflect.resources;

import gov.hhs.onc.dcdt.test.ToolTestListener;
import org.apache.commons.discovery.Resource;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({ ToolTestListener.class })
@Test(dependsOnGroups = { "dcdt.reflect.classloaders" }, groups = { "dcdt.reflect", "dcdt.reflect.resources" })
public class ResourceDiscoveryUtilsTest
{
	@Test
	public void testFindResource()
	{
		Resource resource = ResourceDiscoveryUtils.findResource("version.properties", 
			ResourcePathUtils.packageToPath("gov.hhs.onc.dcdt"));

		Assert.assertNotNull(resource, "Failed to find resource.");
	}
}