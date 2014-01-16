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
        Assert.assertEquals(ToolMethodUtils.getCallee(), getTestCall(), "Callee call does not match the test method call.");
    }

    @Test(dependsOnMethods = { "testGetCallee" })
    public void testGetCaller() {
        Assert.assertEquals(getCallerCall(), getTestCall(), "Caller call does not match the test method call.");
    }

    private static Pair<Class<?>, Method> getCallerCall() {
        return ToolMethodUtils.getCaller();
    }

    private static Pair<Class<?>, Method> getTestCall() {
        return ToolMethodUtils.getCalls().get(1);
    }
}
