package gov.hhs.onc.dcdt.crypto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import gov.hhs.onc.dcdt.crypto.impl.CertificateValidIntervalImpl;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.cert" })
public class CertificateValidIntervalTest {
    private DateFormat formatter;

    @BeforeClass
    public void setUp() {
        formatter = new SimpleDateFormat("MMM dd hh:mm:ss yyyy Z");
    }

    @Test
    public void testCheckValidityCertValid() throws ParseException {
        Date notBefore = formatter.parse("Aug  1 00:00:00 1996 GMT");
        Date notAfter = formatter.parse("Aug  1 00:00:00 2114 GMT");
        CertificateValidInterval validInterval = new CertificateValidIntervalImpl(notBefore, notAfter);
        Assert.assertTrue(validInterval.isValidInterval());
    }

    @Test()
    public void testCheckValidityCertExpired() throws ParseException {
        Date notBefore = formatter.parse("Aug  1 00:00:00 1996 GMT");
        Date notAfter = formatter.parse("Aug  1 00:00:00 2013 GMT");
        CertificateValidInterval validInterval = new CertificateValidIntervalImpl(notBefore, notAfter);
        Assert.assertFalse(validInterval.isValidInterval());
    }

    @Test
    public void testCheckValidityCertNotYetValid() throws ParseException {
        Date notBefore = formatter.parse("Aug  1 00:00:00 2100 GMT");
        Date notAfter = formatter.parse("Aug  1 00:00:00 2101 GMT");
        CertificateValidInterval validInterval = new CertificateValidIntervalImpl(notBefore, notAfter);
        Assert.assertFalse(validInterval.isValidInterval());
    }
}
