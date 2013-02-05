package gov.hhs.onc.dcdt.reflect.resources;

import org.apache.commons.discovery.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.reflect.classLoaders" }, groups = { "dcdt.all", "dcdt.reflect.all", "dcdt.reflect.resources" })
public class ResourceDiscoveryUtilsTest
{
	@Test
	public void testFindResource()
	{
		Resource resource = ResourceDiscoveryUtils.findResource(true, true, "version.properties");

		Assert.assertNotNull(resource, "Failed to find resource.");
	}
}