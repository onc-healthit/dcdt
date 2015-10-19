package gov.hhs.onc.dcdt.service.ldap.logging.impl;

import gov.hhs.onc.dcdt.logging.impl.AbstractLoggingInitializer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class LdapServiceLoggingInitializer extends AbstractLoggingInitializer {
    public LdapServiceLoggingInitializer() {
        super("dcdt-service-ldap", "service-ldap");
    }
}
