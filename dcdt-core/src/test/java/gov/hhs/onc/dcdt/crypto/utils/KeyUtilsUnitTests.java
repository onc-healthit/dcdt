package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all", "dcdt.test.unit.crypto.utils.x500" }, groups = { "dcdt.test.unit.crypto.all",
    "dcdt.test.unit.crypto.utils.all", "dcdt.test.unit.crypto.utils.keys" })
public class KeyUtilsUnitTests extends AbstractToolUnitTests {
    @Test
    public void testPlaceholder() {
        // TODO: implement key read/write tests
    }
}
