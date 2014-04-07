package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import java.util.Date;
import javax.annotation.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all" }, groups = { "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.certs.all",
    "dcdt.test.unit.crypto.certs.valid" })
public class CertificateValidIntervalUnitTests extends AbstractToolUnitTests {
    @Resource(name = "testCertConfigCa1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateConfig testCertConfig;

    @Test
    public void testIsValid() {
        CertificateValidInterval testCertValidInterval = this.testCertConfig.getValidInterval();

        Assert.assertNotNull(testCertValidInterval, "Certificate valid interval is null.");
        Assert.assertTrue(testCertValidInterval.isValid(new Date()), "Current date/time is not within certificate valid interval.");
    }
}
