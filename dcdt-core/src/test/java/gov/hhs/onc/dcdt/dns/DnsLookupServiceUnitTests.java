package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
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
import org.xbill.DNS.Name;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;

@Test(groups = { "dcdt.test.unit.dns.all", "dcdt.test.unit.dns.lookup" })
public class DnsLookupServiceUnitTests extends AbstractToolUnitTests {
    @Autowired
    @DnsResolver(DnsResolverType.EXTERNAL)
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DnsLookupService dnsLookupService;

    @Value("${dcdt.test.dns.lookup.domain.name}")
    private Name testDnsLookupDomainName;

    @Value("${dcdt.test.dns.lookup.domain.cname.1}")
    private Name testDnsLookupDomainCname1;

    @Value("${dcdt.test.dns.lookup.domain.ns.1}")
    private Name testDnsLookupDomainNs1;

    @Value("${dcdt.test.dns.lookup.domain.ns.2}")
    private Name testDnsLookupDomainNs2;

    @Value("${dcdt.test.dns.lookup.domain.srv.1}")
    private Name testDnsLookupDomainSrv1;

    @Value("${dcdt.test.dns.lookup.domain.1.name}")
    private Name testDnsLookupDomain1Name;

    @Test
    public void testGetARecords() throws DnsException {
        List<ARecord> dnsLookupResults = this.dnsLookupService.getARecords(this.testDnsLookupDomain1Name);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getName(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomain1Name));
    }

    @Test
    public void testGetCertRecords() throws DnsException {
        List<CERTRecord> dnsLookupResults = this.dnsLookupService.getCertRecords(this.testDnsLookupDomain1Name);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getCertType(), CertificateType.PKIX);
    }

    @Test
    public void testGetCnameRecords() throws DnsException {
        List<CNAMERecord> dnsLookupResults = this.dnsLookupService.getCnameRecords(this.testDnsLookupDomainCname1);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getTarget(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainName));
    }

    @Test
    public void testGetMxRecords() throws DnsException {
        List<MXRecord> dnsLookupResults = this.dnsLookupService.getMxRecords(this.testDnsLookupDomain1Name);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getTarget(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomain1Name));
    }

    @Test
    public void testGetNsRecords() throws DnsException {
        List<NSRecord> dnsLookupResults = this.dnsLookupService.getNsRecords(this.testDnsLookupDomain1Name);
        Assert.assertEquals(dnsLookupResults.size(), 2);
        Assert.assertEqualsNoOrder(ArrayUtils.toArray(dnsLookupResults.get(0).getTarget(), dnsLookupResults.get(1).getTarget()),
            ArrayUtils.toArray(ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainNs1), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainNs2)));
    }

    @Test
    public void testGetSoaRecords() throws DnsException {
        List<SOARecord> dnsLookupResults = this.dnsLookupService.getSoaRecords(this.testDnsLookupDomain1Name);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getHost(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainNs1));
    }

    @Test
    public void testGetSrvRecords() throws DnsException {
        List<SRVRecord> dnsLookupResults = this.dnsLookupService.getSrvRecords(DnsServiceType.LDAP, DnsServiceProtocol.TCP, this.testDnsLookupDomain1Name);
        Assert.assertEquals(dnsLookupResults.size(), 1);
        Assert.assertEquals(dnsLookupResults.get(0).getTarget(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainSrv1));
    }
}
