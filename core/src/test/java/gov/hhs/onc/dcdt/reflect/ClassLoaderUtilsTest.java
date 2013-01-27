package gov.hhs.onc.dcdt.reflect;

import gov.hhs.onc.dcdt.test.ToolTestListener;
import java.util.Map;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({ ToolTestListener.class })
@Test(groups = { "dcdt.reflect", "dcdt.reflect.classloaders" })
public class ClassLoaderUtilsTest
{
	@Test(dependsOnMethods = { "testGetPackages" })
	public void testGetAllPackages()
	{
		Map<ClassLoader, Set<Package>> pkgsMap = ClassLoaderUtils.getAllPackages();
		
		Assert.assertFalse(pkgsMap.isEmpty(), "Failed to get all packages.");
	}
	
	@Test
	public void testGetPackages()
	{
		Set<Package> pkgs = ClassLoaderUtils.getPackages(ClassLoaderUtils.getContext());
		
		Assert.assertFalse(pkgs.isEmpty(), "Failed to get packages.");
	}
	
	@Test
	public void testGetPackage()
	{
		Package pkg = ClassLoaderUtils.getPackage(ClassLoaderUtilsTest.class.getPackage().getName());

		Assert.assertNotNull(pkg, "Failed to get package.");
	}
}