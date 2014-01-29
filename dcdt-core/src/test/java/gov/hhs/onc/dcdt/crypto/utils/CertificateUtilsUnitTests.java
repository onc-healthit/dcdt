package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all", "dcdt.test.unit.crypto.certs.all", "dcdt.test.unit.crypto.utils.x500",
    "dcdt.test.unit.crypto.utils.keys" }, groups = { "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.utils.all", "dcdt.test.unit.crypto.utils.certs" })
public class CertificateUtilsUnitTests extends AbstractToolUnitTests {
    @Test
    public void testPlaceholder() {
        // TODO: implement key read/write tests
    }
}
