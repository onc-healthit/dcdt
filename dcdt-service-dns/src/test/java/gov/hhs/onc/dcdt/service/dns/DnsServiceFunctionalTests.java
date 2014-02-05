package gov.hhs.onc.dcdt.service.dns;

import gov.hhs.onc.dcdt.service.test.impl.AbstractToolServiceFunctionalTests;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import org.xbill.DNS.TextParseException;

@ContextConfiguration({ "spring/spring-service-dns.xml", "spring/spring-service-dns*.xml" })
@Test(groups = { "dcdt.test.func.service.dns" })
public class DnsServiceFunctionalTests extends AbstractToolServiceFunctionalTests<DnsService> {
    public DnsServiceFunctionalTests() {
        super(DnsService.class);
    }

    @Test(enabled = false)
    public void testPlaceholder() throws TextParseException {
        // TODO: implement
    }

    @Override
    protected DnsService createService() {
        return new DnsService(this.applicationContext);
    }
}
