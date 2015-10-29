package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateIntervalInfoImpl;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all" }, groups = { "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.certs.all",
    "dcdt.test.unit.crypto.certs.interval" })
public class CertificateIntervalUnitTests extends AbstractToolUnitTests {
    @Test
    public void testIsValid() {
        Date testDate = new Date();
        CertificateIntervalInfo testIntervalInfo =
            new CertificateIntervalInfoImpl(DateUtils.addMilliseconds(testDate, -1), DateUtils.addMilliseconds(testDate, 1));

        Assert.assertTrue(testIntervalInfo.isValid(testDate), "Current date/time is not within certificate interval.");
    }
}
