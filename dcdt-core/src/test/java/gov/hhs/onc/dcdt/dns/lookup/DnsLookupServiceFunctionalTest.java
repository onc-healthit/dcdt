package gov.hhs.onc.dcdt.dns.lookup;

import gov.hhs.onc.dcdt.dns.DnsLookupException;
import gov.hhs.onc.dcdt.dns.DnsLookupService;
import gov.hhs.onc.dcdt.dns.DnsResolver;
import gov.hhs.onc.dcdt.dns.DnsResolverType;
import gov.hhs.onc.dcdt.dns.DnsServiceProtocol;
import gov.hhs.onc.dcdt.dns.DnsServiceType;
import gov.hhs.onc.dcdt.test.ToolTestNgFunctionalTests;
import gov.hhs.onc.dcdt.utils.ToolUriUtils;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CERTRecord.CertificateType;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;

@Test(groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.dns.all", "dcdt.test.func.dns.lookup" })
public class DnsLookupServiceFunctionalTest extends ToolTestNgFunctionalTests {
    @Autowired
    @DnsResolver(DnsResolverType.EXTERNAL)
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DnsLookupService dnsLookupService;

    @Value("${dcdt.test.dns.lookup.domain.base}")
    private String testDnsLookupDomainBase;

    @Value("${dcdt.test.dns.lookup.domain.base.cname.1}")
    private String testDnsLookupDomainBaseCname1;

    @Value("${dcdt.test.dns.lookup.domain.base.ns.1}")
    private String testDnsLookupDomainBaseNs1;

    @Value("${dcdt.test.dns.lookup.domain.base.ns.2}")
    private String testDnsLookupDomainBaseNs2;

    @Value("${dcdt.test.dns.lookup.domain.base.srv.1}")
    private String testDnsLookupDomainBaseSrv1;

    @Value("${dcdt.test.dns.lookup.domain.1}")
    private String testDnsLookupDomain1;

    @Test
    public void testGetARecords() throws DnsLookupException {
        List<ARecord> dnsLookupResults = this.dnsLookupService.getARecords(this.testDnsLookupDomain1);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getName().toString(), this.testDnsLookupDomain1 + ToolUriUtils.DOMAIN_DELIM);
    }

    @Test
    public void testGetCertRecords() throws DnsLookupException {
        List<CERTRecord> dnsLookupResults = this.dnsLookupService.getCertRecords(this.testDnsLookupDomain1);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getCertType(), CertificateType.PKIX);
    }

    @Test
    public void testGetCnameRecords() throws DnsLookupException {
        List<CNAMERecord> dnsLookupResults = this.dnsLookupService.getCnameRecords(this.testDnsLookupDomainBaseCname1);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getTarget().toString(), this.testDnsLookupDomainBase + ToolUriUtils.DOMAIN_DELIM);
    }

    @Test
    public void testGetMxRecords() throws DnsLookupException {
        List<MXRecord> dnsLookupResults = this.dnsLookupService.getMxRecords(this.testDnsLookupDomain1);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getTarget().toString(), this.testDnsLookupDomain1 + ToolUriUtils.DOMAIN_DELIM);
    }

    @Test
    public void testGetNsRecords() throws DnsLookupException {
        List<NSRecord> dnsLookupResults = this.dnsLookupService.getNsRecords(this.testDnsLookupDomain1);
        Assert.assertEquals(dnsLookupResults.size(), 2);
        Assert.assertEqualsNoOrder(ArrayUtils.toArray(dnsLookupResults.get(0).getTarget().toString(), dnsLookupResults.get(1).getTarget().toString()),
            ArrayUtils.toArray(this.testDnsLookupDomainBaseNs1 + ToolUriUtils.DOMAIN_DELIM, this.testDnsLookupDomainBaseNs2 + ToolUriUtils.DOMAIN_DELIM));
    }

    @Test
    public void testGetSoaRecords() throws DnsLookupException {
        List<SOARecord> dnsLookupResults = this.dnsLookupService.getSoaRecords(this.testDnsLookupDomain1);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getHost().toString(), this.testDnsLookupDomainBaseNs1 + ToolUriUtils.DOMAIN_DELIM);
    }

    @Test
    public void testGetSrvRecords() throws DnsLookupException {
        List<SRVRecord> dnsLookupResults = this.dnsLookupService.getSrvRecords(DnsServiceType.LDAP, DnsServiceProtocol.TCP, this.testDnsLookupDomain1);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getTarget().toString(), this.testDnsLookupDomainBaseSrv1 + ToolUriUtils.DOMAIN_DELIM);
    }
}
