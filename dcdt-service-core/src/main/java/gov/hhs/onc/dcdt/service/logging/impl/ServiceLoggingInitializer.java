package gov.hhs.onc.dcdt.service.logging.impl;

import gov.hhs.onc.dcdt.logging.impl.AbstractLoggingInitializer;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE + 2)
public class ServiceLoggingInitializer extends AbstractLoggingInitializer {
    private final static String LOG_DIR_PATH_DEFAULT = SystemUtils.USER_DIR + "/logs";

    public ServiceLoggingInitializer(AbstractRefreshableConfigApplicationContext appContext) {
        super(appContext);
    }

    @Override
    public String buildLogDirectoryPath() {
        return ObjectUtils.defaultIfNull(super.buildLogDirectoryPath(), LOG_DIR_PATH_DEFAULT);
    }
}
