package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.ToolException;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.unit.utils.all", "dcdt.test.unit.utils.pkg" })
public class ToolPackageUtilsUnitTests extends AbstractToolUnitTests {
    private final static Class<?> TEST_PKG_CLASS = ToolPackageUtils.class;
    private final static Package TEST_PKG = TEST_PKG_CLASS.getPackage();
    private final static String TEST_PKG_NAME = TEST_PKG.getName();

    private final static Package TEST_PARENT_PKG = ToolException.class.getPackage();

    @Test
    public void testGetName() {
        Assert.assertEquals(ToolPackageUtils.getName(TEST_PKG_CLASS), TEST_PKG_NAME, "Unable to get package name from class.");
        Assert.assertEquals(ToolPackageUtils.getName(TEST_PKG), TEST_PKG_NAME, "Unable to get package name from package.");
    }

    @Test(dependsOnMethods = { "testGetName" })
    public void testGetParent() {
        Assert.assertEquals(ToolPackageUtils.getParent(TEST_PKG), TEST_PARENT_PKG, "Unable to get parent package.");
    }
}
