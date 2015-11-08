package gov.hhs.onc.dcdt.service.ldap.context.impl;

import gov.hhs.onc.dcdt.context.impl.AbstractMetadataInitializer;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class LdapServiceMetadataInitializer extends AbstractMetadataInitializer {
    public LdapServiceMetadataInitializer(AbstractRefreshableConfigApplicationContext appContext) {
        super(appContext, "service-ldap", "dcdt-service-ldap");
    }
}
