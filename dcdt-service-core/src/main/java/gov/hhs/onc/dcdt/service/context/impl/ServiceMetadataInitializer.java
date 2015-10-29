package gov.hhs.onc.dcdt.service.context.impl;

import gov.hhs.onc.dcdt.context.impl.AbstractMetadataInitializer;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServiceMetadataInitializer extends AbstractMetadataInitializer<AbstractRefreshableConfigApplicationContext> {
    public ServiceMetadataInitializer(AbstractRefreshableConfigApplicationContext appContext) {
        super(appContext, "service", "dcdt-service-core");
    }
}
