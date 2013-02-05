package gov.hhs.onc.dcdt.config;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.config" }, groups = { "dcdt.all", "dcdt.config.all", "dcdt.config.version" })
public class ToolVersionTest
{
	private static ToolVersion coreVersion;
	
	@Test(dependsOnMethods = { "testGetProjectValues" })
	public void testGetSvnValues()
	{
		Assert.assertNotNull(coreVersion.getSvnAuthor(), "Failed to get core module SVN author.");
		Assert.assertNotNull(coreVersion.getSvnDate(), "Failed to get core module SVN date.");
		Assert.assertNotNull(coreVersion.getSvnHeadUrl(), "Failed to get core module SVN head URL.");
		Assert.assertNotNull(coreVersion.getSvnRevision(), "Failed to get core module SVN revision.");
	}
	
	@Test(dependsOnMethods = { "testGetProjectValues" })
	public void testGetBuildValues()
	{
		Assert.assertNotNull(coreVersion.getBuildTimestamp(), "Failed to get core module build timestamp.");
	}
	
	@Test
	public void testGetProjectValues()
	{
		coreVersion = ToolConfigTest.config.getModuleVersions().get(ToolConfig.CORE_MODULE_NAME);
		
		Assert.assertNotNull(coreVersion.getGroupId(), "Failed to get core module group ID.");
		Assert.assertNotNull(coreVersion.getArtifactId(), "Failed to get core module artifact ID.");
		Assert.assertNotNull(coreVersion.getVersion(), "Failed to get core module version.");
		Assert.assertNotNull(coreVersion.getName(), "Failed to get core module name.");
		Assert.assertNotNull(coreVersion.getDescription(), "Failed to get core module description.");
	}
}