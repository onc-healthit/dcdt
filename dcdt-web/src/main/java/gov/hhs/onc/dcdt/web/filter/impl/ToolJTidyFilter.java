package gov.hhs.onc.dcdt.web.filter.impl;

import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import gov.hhs.onc.dcdt.web.utils.ToolContentTypeUtils;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.ServletWebRequest;
import org.w3c.tidy.servlet.filter.JTidyFilter;

public class ToolJTidyFilter extends JTidyFilter {
    private final static String DELIM_CONFIG_ENTRY = "; ";
    private final static String DELIM_CONFIG_ENTRY_PAIR = ": ";

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolJTidyFilter.class);

    @Autowired
    private ContentNegotiationManager contentNegotiationManager;

    private Map<String, Object> config = new LinkedHashMap<>();
    private Set<MediaType> filterContentTypes = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) {
        MutableFilterConfig mutableFilterConfig = new MutableFilterConfig(filterConfig);
        ToolStrBuilder configStrBuilder = new ToolStrBuilder();

        for (String configPropName : this.config.keySet()) {
            configStrBuilder.appendDelimiter(DELIM_CONFIG_ENTRY);
            configStrBuilder.append(configPropName);
            configStrBuilder.appendDelimiter(DELIM_CONFIG_ENTRY_PAIR);
            configStrBuilder.append(Objects.toString(this.config.get(configPropName), StringUtils.EMPTY));
        }

        mutableFilterConfig.getInitParameters().put(CONFIG_CONFIG, configStrBuilder.build());

        super.init(mutableFilterConfig);

        LOGGER.debug(String.format("Initialized Spring JTidy servlet filter (name=%s): {%s}", mutableFilterConfig.getFilterName(),
            ToolStringUtils.joinDelimit(mutableFilterConfig.getInitParameters().entrySet(), ", ")));
    }

    @Override
    public void destroy() {
        super.destroy();

        LOGGER.debug("Destroyed Spring JTidy servlet filter.");
    }

    @Override
    public void doFilter(ServletRequest servletReq, ServletResponse servletResp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletReq = (HttpServletRequest) servletReq;
        MediaType servletRespContentType;

        if ((servletRespContentType =
            ToolContentTypeUtils.findIncludes(this.filterContentTypes, this.contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(httpServletReq)))) != null) {
            super.doFilter(servletReq, servletResp, filterChain);

            LOGGER.trace(String.format("Filtered servlet request (uri=%s) content (contentType=%s) using Spring JTidy servlet filter.",
                httpServletReq.getRequestURI(), servletRespContentType));
        } else {
            filterChain.doFilter(servletReq, servletResp);
        }
    }

    public Map<String, Object> getConfig() {
        return this.config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config.clear();
        this.config.putAll(config);
    }

    public Set<MediaType> getFilterContentTypes() {
        return this.filterContentTypes;
    }

    public void setFilterContentTypes(Set<MediaType> filterContentTypes) {
        this.filterContentTypes.clear();
        this.filterContentTypes.addAll(filterContentTypes);
    }
}
