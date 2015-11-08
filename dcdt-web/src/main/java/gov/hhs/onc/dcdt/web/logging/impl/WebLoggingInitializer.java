package gov.hhs.onc.dcdt.web.logging.impl;

import gov.hhs.onc.dcdt.context.ToolProperties;
import gov.hhs.onc.dcdt.logging.impl.AbstractLoggingInitializer;
import java.nio.file.Paths;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebLoggingInitializer extends AbstractLoggingInitializer {
    private final static String TOMCAT_LOG_DIR_PATH = "logs";

    public WebLoggingInitializer(AbstractRefreshableConfigApplicationContext appContext) {
        super(appContext);
    }

    @Nullable
    @Override
    public String buildLogDirectoryPath() {
        String logDirPath = super.buildLogDirectoryPath();

        if (logDirPath == null) {
            String baseLogDirPath = System.getProperty(ToolProperties.CATALINA_BASE_NAME);

            if (StringUtils.isBlank(baseLogDirPath)) {
                throw new ApplicationContextException("Unable to determine base log file directory.");
            }

            logDirPath = Paths.get(baseLogDirPath, TOMCAT_LOG_DIR_PATH).toString();
        }

        return logDirPath;
    }
}
