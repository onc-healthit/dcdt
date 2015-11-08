package gov.hhs.onc.dcdt.service.http.context.impl;

import gov.hhs.onc.dcdt.context.impl.AbstractMetadataInitializer;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class HttpServiceMetadataInitializer extends AbstractMetadataInitializer {
    public HttpServiceMetadataInitializer(AbstractRefreshableConfigApplicationContext appContext) {
        super(appContext, "service-http", "dcdt-service-http");
    }
}
