package gov.hhs.onc.dcdt.service.context.impl;

import gov.hhs.onc.dcdt.context.impl.AbstractMetadataInitializer;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE + 2)
public class ServiceMetadataInitializer extends AbstractMetadataInitializer {
    public ServiceMetadataInitializer(AbstractRefreshableConfigApplicationContext appContext) {
        super(appContext, "service", "dcdt-service-core");
    }
}
