package gov.hhs.onc.dcdt.web.context.impl;

import gov.hhs.onc.dcdt.context.ToolProperties;
import gov.hhs.onc.dcdt.context.impl.AbstractMetadataInitializer;
import gov.hhs.onc.dcdt.net.utils.ToolUriUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.WebApplicationContext;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebMetadataInitializer extends AbstractMetadataInitializer {
    private final static String DEFAULT_HOME_DIR_CONTEXT_PATH_SUFFIX = ToolUriUtils.PATH_DELIM + "WEB-INF";

    public WebMetadataInitializer(AbstractRefreshableConfigApplicationContext appContext) {
        super(appContext, "web", "dcdt-web");
    }

    @Override
    public String buildHomeDirectoryPath() {
        ConfigurableEnvironment env = this.appContext.getEnvironment();

        return (env.containsProperty(ToolProperties.APP_HOME_DIR_NAME)
            ? env.getProperty(ToolProperties.APP_HOME_DIR_NAME) : ((this.appContext instanceof WebApplicationContext)
                ? (((WebApplicationContext) this.appContext).getServletContext().getRealPath(ToolUriUtils.PATH_DELIM) + DEFAULT_HOME_DIR_CONTEXT_PATH_SUFFIX)
                : SystemUtils.USER_DIR));
    }
}
