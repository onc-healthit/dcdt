package gov.hhs.onc.dcdt.service.dns;

import gov.hhs.onc.dcdt.dns.DnsResolver;
import gov.hhs.onc.dcdt.service.test.ToolServiceTestNgFunctionalTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import org.xbill.DNS.TextParseException;

@ContextConfiguration({ "spring/spring-service-dns.xml", "spring/spring-service-dns*.xml" })
@Test(groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.service.all", "dcdt.test.func.service.dns" })
public class DnsServiceFunctionalTests extends ToolServiceTestNgFunctionalTests<DnsService> {
    public DnsServiceFunctionalTests() {
        super(DnsService.class);
    }

    @Test
    public void testPlaceholder() throws TextParseException {
        // TODO: implement
    }

    @Override
    protected DnsService createService() {
        return new DnsService(this.applicationContext);
    }
}
