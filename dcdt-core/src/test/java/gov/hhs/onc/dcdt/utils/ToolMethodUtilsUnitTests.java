package gov.hhs.onc.dcdt.utils;


import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import java.lang.reflect.Method;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.utils.all", "dcdt.test.unit.utils.method" })
public class ToolMethodUtilsUnitTests extends ToolTestNgUnitTests {
    @Test
    public void testGetCallee() {
        Pair<Class<?>, Method> callee = ToolMethodUtils.getCallee();
        Assert.assertNotNull(callee, "Unable to get method callee.");

        Assert.assertEquals(callee.getLeft(), ToolMethodUtilsUnitTests.class, "Method callee class is incorrect.");

        Method calleeMethod = callee.getRight();
        Assert.assertNotNull(calleeMethod, "Unable to get method callee method.");
        Assert.assertEquals(calleeMethod.getName(), "testGetCallee", "Method callee method name is incorrect.");
    }

    @Test(dependsOnMethods = { "testGetCallee" })
    public void testGetCaller() {
        Pair<Class<?>, Method> caller = getCallerMethod();
        Assert.assertNotNull(caller, "Unable to get method caller.");

        Assert.assertEquals(caller.getLeft(), ToolMethodUtilsUnitTests.class, "Method caller class is incorrect.");

        Method callerMethod = caller.getRight();
        Assert.assertNotNull(callerMethod, "Unable to get method caller method.");
        Assert.assertEquals(callerMethod.getName(), "testGetCaller", "Method caller method name is incorrect.");
    }

    private static Pair<Class<?>, Method> getCallerMethod() {
        return ToolMethodUtils.getCaller();
    }
}
