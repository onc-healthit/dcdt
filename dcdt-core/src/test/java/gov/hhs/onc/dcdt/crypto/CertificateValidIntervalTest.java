package gov.hhs.onc.dcdt.crypto;

import java.security.Security;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import gov.hhs.onc.dcdt.crypto.impl.CertificateValidIntervalImpl;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.cert" })
public class CertificateValidIntervalTest {
    private DateFormat formatter;

    @BeforeTest
    public void setUp() {
        Security.addProvider(new BouncyCastleProvider());
        formatter = new SimpleDateFormat("MMM dd hh:mm:ss yyyy Z");
    }

    @Test
    public void testCheckValidityCertValid() throws ParseException {
        Date notBefore = formatter.parse("Aug  1 00:00:00 1996 GMT");
        Date notAfter = formatter.parse("Aug  1 00:00:00 2114 GMT");
        CertificateValidInterval validInterval = new CertificateValidIntervalImpl(notBefore, notAfter);

        try {
            Assert.assertTrue(validInterval.isValidInterval());
        } catch (CertificateException e) {
            Assert.fail("Certificate interval is valid.");
        }
    }

    @Test()
    public void testCheckValidityCertExpired() throws ParseException {
        String expectedMessage = "Certificate expired.";
        Date notBefore = formatter.parse("Aug  1 00:00:00 1996 GMT");
        Date notAfter = formatter.parse("Aug  1 00:00:00 2013 GMT");
        CertificateValidInterval validInterval = new CertificateValidIntervalImpl(notBefore, notAfter);

        try {
            validInterval.isValidInterval();
            Assert.fail(expectedMessage);
        } catch (CertificateException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testCheckValidityCertNotYetValid() throws ParseException {
        String expectedMessage = "Certificate not yet valid.";
        Date notBefore = formatter.parse("Aug  1 00:00:00 2100 GMT");
        Date notAfter = formatter.parse("Aug  1 00:00:00 2101 GMT");
        CertificateValidInterval validInterval = new CertificateValidIntervalImpl(notBefore, notAfter);

        try {
            validInterval.isValidInterval();
            Assert.fail(expectedMessage);
        } catch (CertificateException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }
}
