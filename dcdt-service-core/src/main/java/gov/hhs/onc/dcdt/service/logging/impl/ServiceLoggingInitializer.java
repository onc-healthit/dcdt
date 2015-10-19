package gov.hhs.onc.dcdt.service.logging.impl;

import gov.hhs.onc.dcdt.logging.impl.AbstractLoggingInitializer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE + 2)
public class ServiceLoggingInitializer extends AbstractLoggingInitializer {
    public ServiceLoggingInitializer() {
        super("dcdt-service-core", "service");
    }
}
