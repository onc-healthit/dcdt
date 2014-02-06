package gov.hhs.onc.dcdt.web.filter.impl;

import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.Hashtable;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

public class ToolUrlRewriteFilter extends UrlRewriteFilter {
    private final static String INIT_PARAM_NAME_CONF_PATH = "confPath";
    private final static String INIT_PARAM_NAME_CONF_RELOAD_CHECK_INTERVAL = "confReloadCheckInterval";
    private final static String INIT_PARAM_NAME_STATUS_PATH = "statusPath";

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolUrlRewriteFilter.class);

    private String confPath;
    private long confReloadCheckInterval;
    private String statusPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        MutableFilterConfig mutableFilterConfig = new MutableFilterConfig(filterConfig);

        Hashtable<String, Object> filterConfigInitParams = mutableFilterConfig.getInitParameters();
        filterConfigInitParams.put(INIT_PARAM_NAME_CONF_PATH, this.confPath);
        filterConfigInitParams.put(INIT_PARAM_NAME_CONF_RELOAD_CHECK_INTERVAL, this.confReloadCheckInterval);
        filterConfigInitParams.put(INIT_PARAM_NAME_STATUS_PATH, this.statusPath);

        super.init(mutableFilterConfig);

        LOGGER.debug(String.format("Initialized Spring URL rewrite servlet filter (name=%s): {%s}", mutableFilterConfig.getFilterName(),
            ToolStringUtils.joinDelimit(mutableFilterConfig.getInitParameters().entrySet(), ", ")));
    }

    @Override
    public void destroy() {
        super.destroy();

        LOGGER.debug("Destroyed Spring URL rewrite servlet filter.");
    }

    public String getConfPath() {
        return confPath;
    }

    public void setConfPath(String confPath) {
        this.confPath = confPath;
    }

    public void setConfReloadCheckInterval(long confReloadCheckInterval) {
        this.confReloadCheckInterval = confReloadCheckInterval;
    }

    public void setStatusPath(String statusPath) {
        this.statusPath = statusPath;
    }
}
