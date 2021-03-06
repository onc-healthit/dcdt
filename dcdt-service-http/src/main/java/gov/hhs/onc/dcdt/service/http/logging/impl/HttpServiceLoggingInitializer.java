package gov.hhs.onc.dcdt.service.http.logging.impl;

import gov.hhs.onc.dcdt.logging.impl.AbstractLoggingInitializer;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class HttpServiceLoggingInitializer extends AbstractLoggingInitializer {
    public HttpServiceLoggingInitializer(AbstractRefreshableConfigApplicationContext appContext) {
        super(appContext);
    }
}
