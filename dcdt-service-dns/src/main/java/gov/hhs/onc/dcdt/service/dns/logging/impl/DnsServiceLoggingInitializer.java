package gov.hhs.onc.dcdt.service.dns.logging.impl;

import gov.hhs.onc.dcdt.logging.impl.AbstractLoggingInitializer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class DnsServiceLoggingInitializer extends AbstractLoggingInitializer {
    public DnsServiceLoggingInitializer() {
        super("dcdt-service-dns", "service-dns");
    }
}
