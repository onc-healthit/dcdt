package gov.hhs.onc.dcdt.dns.lookup;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsResultType;
import gov.hhs.onc.dcdt.dns.DnsServiceProtocol;
import gov.hhs.onc.dcdt.dns.DnsServiceType;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
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
import org.xbill.DNS.Record;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;

@Test(groups = { "dcdt.test.unit.dns.all", "dcdt.test.unit.dns.lookup.all", "dcdt.test.unit.dns.lookup.service" })
public class DnsLookupServiceUnitTests extends AbstractToolUnitTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DnsLookupService dnsLookupService;

    @Value("${dcdt.test.dns.lookup.domain.name}")
    private Name testDnsLookupDomainName;

    @Value("${dcdt.test.dns.lookup.domain.name.2}")
    private Name testDnsLookupDomainName2;

    @Value("${dcdt.test.dns.lookup.domain.cname.1}")
    private Name testDnsLookupDomainCname1;

    @Value("${dcdt.test.dns.lookup.domain.cname.2}")
    private Name testDnsLookupDomainCname2;

    @Value("${dcdt.test.dns.lookup.domain.ns.1}")
    private Name testDnsLookupDomainNs1;

    @Value("${dcdt.test.dns.lookup.domain.ns.2}")
    private Name testDnsLookupDomainNs2;

    @Value("${dcdt.test.dns.lookup.domain.srv.1}")
    private Name testDnsLookupDomainSrv1;

    @Value("${dcdt.test.dns.lookup.domain.1.name}")
    private Name testDnsLookupDomain1Name;

    @Test
    public void testLookupARecords() throws DnsException {
        DnsLookupResult<ARecord> result = assertResultValid(this.dnsLookupService.lookupARecords(this.testDnsLookupDomain1Name), 1);
        // noinspection ConstantConditions
        Assert.assertEquals(result.getAnswers().get(0).getName(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomain1Name));
    }

    @Test
    public void testLookupCertRecords() throws DnsException {
        DnsLookupResult<CERTRecord> result = assertResultValid(this.dnsLookupService.lookupCertRecords(this.testDnsLookupDomain1Name), 1);
        // noinspection ConstantConditions
        Assert.assertEquals(result.getAnswers().get(0).getCertType(), CertificateType.PKIX);
    }

    @Test
    public void testLookupCnameRecords() throws DnsException {
        DnsLookupResult<CNAMERecord> cnameRecordResult1 = assertResultValid(this.dnsLookupService.lookupCnameRecords(this.testDnsLookupDomainCname1), 1);
        // noinspection ConstantConditions
        Assert.assertEquals(cnameRecordResult1.getAnswers().get(0).getTarget(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainName));
        DnsLookupResult<CNAMERecord> cnameRecordResult2 = assertResultValid(this.dnsLookupService.lookupCnameRecords(this.testDnsLookupDomainCname2), 1);
        // noinspection ConstantConditions
        Assert.assertEquals(cnameRecordResult2.getAnswers().get(0).getTarget(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainName2));
        DnsLookupResult<ARecord> aRecordResult = assertResultValid(this.dnsLookupService.lookupARecords(this.testDnsLookupDomainCname2), 1);
        // noinspection ConstantConditions
        Assert.assertEquals(aRecordResult.getAnswers().get(0).getName(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainName2));
    }

    @Test
    public void testLookupMxRecords() throws DnsException {
        DnsLookupResult<MXRecord> result = assertResultValid(this.dnsLookupService.lookupMxRecords(this.testDnsLookupDomain1Name), 1);
        // noinspection ConstantConditions
        Assert.assertEquals(result.getAnswers().get(0).getTarget(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomain1Name));
    }

    @Test
    public void testLookupNsRecords() throws DnsException {
        DnsLookupResult<NSRecord> result = assertResultValid(this.dnsLookupService.lookupNsRecords(this.testDnsLookupDomain1Name), 2);
        // noinspection ConstantConditions
        Assert.assertEqualsNoOrder(ArrayUtils.toArray(result.getAnswers().get(0).getTarget(), result.getAnswers().get(1).getTarget()),
            ArrayUtils.toArray(ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainNs1), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainNs2)));
    }

    @Test
    public void testLookupSoaRecords() throws DnsException {
        DnsLookupResult<SOARecord> result = assertResultValid(this.dnsLookupService.lookupSoaRecords(this.testDnsLookupDomain1Name), 1);
        // noinspection ConstantConditions
        Assert.assertEquals(result.getAnswers().get(0).getHost(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainNs1));
    }

    @Test
    public void testLookupSrvRecords() throws DnsException {
        DnsLookupResult<SRVRecord> result =
            assertResultValid(this.dnsLookupService.lookupSrvRecords(DnsServiceType.LDAP, DnsServiceProtocol.TCP, this.testDnsLookupDomain1Name), 1);
        // noinspection ConstantConditions
        Assert.assertEquals(result.getAnswers().get(0).getTarget(), ToolDnsNameUtils.toAbsolute(this.testDnsLookupDomainSrv1));
    }

    private static <T extends Record> DnsLookupResult<T> assertResultValid(DnsLookupResult<T> result, int answersNum) {
        DnsResultType resultType = result.getType();

        Assert.assertTrue(resultType.isSuccess(), String.format("DNS lookup (recordType=%s, questionName=%s) error (type=%s): [%s]", result.getRecordType()
            .name(), result.getQuestionName(), resultType.name(), ToolStringUtils.joinDelimit(result.getMessages(), "; ")));
        Assert.assertTrue(
            result.hasAnswers(),
            String.format("DNS lookup (recordType=%s, questionName=%s) does not have any resolved answers.", result.getRecordType().name(),
                result.getQuestionName()));
        // noinspection ConstantConditions
        Assert.assertEquals(
            result.getAnswers().size(),
            answersNum,
            String.format("DNS lookup (recordType=%s, questionName=%s) resolved answers count does not match.", result.getRecordType().name(),
                result.getQuestionName()));

        return result;
    }
}
