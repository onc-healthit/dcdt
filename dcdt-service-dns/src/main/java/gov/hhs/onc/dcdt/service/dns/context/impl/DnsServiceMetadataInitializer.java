package gov.hhs.onc.dcdt.service.dns.context.impl;

import gov.hhs.onc.dcdt.context.impl.AbstractMetadataInitializer;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class DnsServiceMetadataInitializer extends AbstractMetadataInitializer {
    public DnsServiceMetadataInitializer(AbstractRefreshableConfigApplicationContext appContext) {
        super(appContext, "service-dns", "dcdt-service-dns");
    }
}
