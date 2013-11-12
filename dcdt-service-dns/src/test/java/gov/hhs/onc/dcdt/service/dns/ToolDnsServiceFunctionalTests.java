package gov.hhs.onc.dcdt.service.dns;

import gov.hhs.onc.dcdt.dns.DnsResolver;
import gov.hhs.onc.dcdt.dns.DnsResolverType;
import gov.hhs.onc.dcdt.dns.lookup.ToolDnsLookupService;
import gov.hhs.onc.dcdt.service.test.ToolServiceTestNgFunctionalTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xbill.DNS.Name;
import org.xbill.DNS.TextParseException;

@ContextConfiguration({ "spring/spring-service-dns*.xml" })
@Test(groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.service.all", "dcdt.test.func.service.dns" })
public class ToolDnsServiceFunctionalTests extends ToolServiceTestNgFunctionalTests<ToolDnsService> {
    @Autowired
    @DnsResolver(DnsResolverType.LOCAL)
    private ToolDnsLookupService dnsLookupServiceLocal;

    public ToolDnsServiceFunctionalTests() {
        super(ToolDnsService.class);
    }

    @Test
    public void testLookupAddress() throws TextParseException {
        Assert.assertNotNull(this.dnsLookupServiceLocal.lookupAddress(new Name("google.com")).getAddress(), "Unable to lookup address.");
    }

    @Override
    protected ToolDnsService createService() {
        return new ToolDnsService(this.applicationContext);
    }
}
