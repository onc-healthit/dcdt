package gov.hhs.onc.dcdt.dns.lookup;

import gov.hhs.onc.dcdt.test.ToolTestNgFunctionalTests;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.dns.all", "dcdt.test.func.dns.services" })
public class DnsLookupServiceFunctionalTest extends ToolTestNgFunctionalTests {
    @Test
    public void testPlaceholder() {
        // TODO: implement
    }

    // @formatter:off
    /*
    @Autowired
    private DnsLookupService dnsLookupService;

    private static final String ROOT = ".";
    private static final String DEMO_DIRECT_TEST_DOMAIN = "demo.direct-test.com";
    private static final String DIRECT1_DEMO_DIRECT_TEST_DOMAIN = "direct1." + DEMO_DIRECT_TEST_DOMAIN;

    @Test
    public void testGetARecords() throws Exception {
        ARecord[] aRecords = dnsLookupService.getARecords(false, DEMO_DIRECT_TEST_DOMAIN);
        Assert.assertNotNull(aRecords);
        Assert.assertEquals(aRecords.length, 1);
        Assert.assertEquals(aRecords[0].getName().toString(), DEMO_DIRECT_TEST_DOMAIN + ROOT);
    }

    @Test
    public void testGetMxRecords() throws Exception {
        MXRecord[] mxRecords = dnsLookupService.getMxRecords(false, DEMO_DIRECT_TEST_DOMAIN);
        Assert.assertNotNull(mxRecords);
        Assert.assertEquals(mxRecords.length, 1);
        Assert.assertEquals(mxRecords[0].getTarget().toString(), DEMO_DIRECT_TEST_DOMAIN + ROOT);
    }

    @Test
    public void testGetCertRecords() throws Exception {
        CERTRecord[] certRecords = dnsLookupService.getCertRecords(false, DIRECT1_DEMO_DIRECT_TEST_DOMAIN);
        Assert.assertNotNull(certRecords);
        Assert.assertEquals(certRecords.length, 1);
        Assert
            .assertEquals(
                certRecords[0].rdataToString(),
                "1 12179 5 MIICmjCCAgOgAwIBAgIIXIh/1/YS7eYwDQYJKoZIhvcNAQEFBQAwRDEgMB4GA1UEAwwXZGVtby5kaXJlY3QtdGVzdC5jb21fY2ExIDAeBgNVBAoMF2RlbW8uZGlyZWN0LXRlc3QuY29tX2NhMB4XDTEzMDcxMjIzMjYwMVoXDTE0MDcxMjIzMjYwMVowYzErMCkGCSqGSIb3DQEJARYcZGlyZWN0MS5kZW1vLmRpcmVjdC10ZXN0LmNvbTEVMBMGA1UEAwwMZHRzNTAxX3ZhbGlkMR0wGwYDVQQKDBRkZW1vLmRpcmVjdC10ZXN0LmNvbTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEA8JbvDgvm34rwOg8gsQuzrM/z6XxjhmfYqUviWCao2NbckY3WgiIq5kWzBCApF0bRTIEDscD5YZL6HTHtyuWiSK0ecT9K3p+tA8Oau3Mg/pKh9Go50xwYixn+NdqN3JDDGiqxKKR2CjFjtoHtfb7PSyM6o21z++17/GnsI6u7L5MCAwEAAaN2MHQwCQYDVR0TBAIwADAfBgNVHSMEGDAWgBQQZDWHuEFKK57sUDt6qCnME22RCTAdBgNVHQ4EFgQUrTBy4hGrahZ/vubOozM/PcclRlkwJwYDVR0RBCAwHoIcZGlyZWN0MS5kZW1vLmRpcmVjdC10ZXN0LmNvbTANBgkqhkiG9w0BAQUFAAOBgQAdayxQIYdIcCmOsm0Ma/nVI2WUmnE2ji0jh7e7ROGS1S/Q746T96tktXdOtOEQgzBe+WBeLWvkZDXjmDEVVceSDC5iEEuEnP+RKF70Hwe6Sv3A247KDeGfXz0up7d20caLKQqYikeObuVtsqGC2txyiBhqCMnvvuVnf3AqH1OiAA==");
    }

    @Test
    public void testGetSoaRecords() throws Exception {
        SOARecord[] soaRecords = dnsLookupService.getSoaRecords(false, DEMO_DIRECT_TEST_DOMAIN);
        Assert.assertNotNull(soaRecords);
        Assert.assertEquals(soaRecords.length, 2);
        Assert.assertEquals(soaRecords[0].getHost().toString(), "ns1." + DEMO_DIRECT_TEST_DOMAIN + ROOT);
    }

    @Test
    public void testGetSrvRecords() throws Exception {
        SRVRecord[] srvRecords = dnsLookupService.getSrvRecords(false, ServiceType.LDAP, ServiceProtocol.TCP, DIRECT1_DEMO_DIRECT_TEST_DOMAIN);
        Assert.assertNotNull(srvRecords);
        Assert.assertEquals(srvRecords.length, 1);
        SRVRecord srvRecord = srvRecords[0];
        Assert.assertEquals(srvRecord.getTarget().toString(), "ldap." + DEMO_DIRECT_TEST_DOMAIN + ROOT);
    }

    @Test
    public void testGetNsRecords() throws Exception {
        NSRecord[] nsRecords = dnsLookupService.getNsRecords(false, DEMO_DIRECT_TEST_DOMAIN);
        Assert.assertNotNull(nsRecords);
        Assert.assertEquals(nsRecords.length, 2);
        Assert.assertTrue(nsRecordIsPresent("ns1." + DEMO_DIRECT_TEST_DOMAIN + ROOT, nsRecords));
        Assert.assertTrue(nsRecordIsPresent("ns2." + DEMO_DIRECT_TEST_DOMAIN + ROOT, nsRecords));
    }

    @Test
    public void testGetCNameRecords() throws Exception {
        CNAMERecord[] cnameRecords = dnsLookupService.getCNameRecords(false, "www." + DEMO_DIRECT_TEST_DOMAIN);
        Assert.assertNotNull(cnameRecords);
        Assert.assertEquals(cnameRecords.length, 1);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullDomainString() throws Exception {
        dnsLookupService.getARecords(false, null);
    }

    @Test(expectedExceptions = DnsLookupException.class)
    public void testEmptyDomainString() throws Exception {
        dnsLookupService.getARecords(false, "");
    }

    private boolean nsRecordIsPresent(final String targetNsRecord, NSRecord[] nsRecords) {
        for (NSRecord nsRecord : nsRecords) {
            if (nsRecord.getTarget().toString().equals(targetNsRecord)) {
                return true;
            }
        }
        return false;
    }
    */
    // @formatter:on
}
