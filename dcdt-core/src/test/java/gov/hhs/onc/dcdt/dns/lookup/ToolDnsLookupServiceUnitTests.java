package gov.hhs.onc.dcdt.dns.lookup;


import gov.hhs.onc.dcdt.dns.DnsResolver;
import gov.hhs.onc.dcdt.dns.DnsResolverType;
import gov.hhs.onc.dcdt.test.ToolTestNgTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xbill.DNS.Name;
import org.xbill.DNS.TextParseException;

@ContextHierarchy({ @ContextConfiguration({ "classpath*:META-INF/spring/spring-core.xml", "classpath*:spring/spring-core.xml" }) })
public class ToolDnsLookupServiceUnitTests extends ToolTestNgTests {
    @Autowired
    @DnsResolver(DnsResolverType.EXTERNAL)
    private ToolDnsLookupService dnsLookupServiceExternal;

    @Test
    public void testLookupAddress() throws TextParseException {
        Assert.assertNotNull(this.dnsLookupServiceExternal.lookupAddress(new Name("google.com")).getAddress(), "Unable to lookup address.");
    }
}
