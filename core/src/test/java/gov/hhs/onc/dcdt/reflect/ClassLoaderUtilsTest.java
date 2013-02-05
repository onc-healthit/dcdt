package gov.hhs.onc.dcdt.reflect;

import java.util.Map;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.all", "dcdt.reflect.all", "dcdt.reflect.classLoaders" })
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