package gov.hhs.onc.dcdt.service.dns;


import gov.hhs.onc.dcdt.dns.DnsResolver;
import gov.hhs.onc.dcdt.dns.DnsResolverType;
import gov.hhs.onc.dcdt.dns.lookup.ToolDnsLookupService;
import gov.hhs.onc.dcdt.service.test.ToolServiceTestNgFunctionalTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xbill.DNS.Name;
import org.xbill.DNS.TextParseException;

@ContextHierarchy({ @ContextConfiguration({ "classpath*:META-INF/spring/spring-service-dns*.xml", "classpath*:spring/spring-service-dns*.xml" }) })
public class ToolDnsServiceFunctionalTests extends ToolServiceTestNgFunctionalTests<ClassPathXmlApplicationContext, ToolDnsService> {
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
