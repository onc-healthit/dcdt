package gov.hhs.onc.dcdt.web.filter.impl;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import gov.hhs.onc.dcdt.web.filter.WebContentFilter;
import gov.hhs.onc.dcdt.web.media.utils.ToolMediaTypeUtils;
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

public class ToolJTidyFilter extends JTidyFilter implements WebContentFilter {
    private final static String DELIM_CONFIG_ENTRY = "; ";
    private final static String DELIM_CONFIG_ENTRY_PAIR = ": ";

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolJTidyFilter.class);

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ContentNegotiationManager contentNegotiationManager;

    private Map<String, Object> config = new LinkedHashMap<>();
    private Set<MediaType> contentTypes = new HashSet<>();

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
        HttpServletRequest httpServletReq = ((HttpServletRequest) servletReq);
        String servletRespContentTypeStr = servletResp.getContentType();
        MediaType servletRespContentType;

        if (((servletRespContentType =
            ToolMediaTypeUtils.findIncluded(
                this.contentTypes,
                ((servletRespContentTypeStr != null)
                    ? ToolArrayUtils.asSet(MediaType.parseMediaType(servletRespContentTypeStr)) : this.contentNegotiationManager
                        .resolveMediaTypes(new ServletWebRequest(httpServletReq))))) != null)
            && servletRespContentType.isConcrete()) {
            super.doFilter(servletReq, servletResp, filterChain);

            LOGGER.trace(String.format("Servlet request (uri=%s) content (contentType={%s}) filtered using Spring JTidy formatting.",
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

    @Override
    public Set<MediaType> getContentTypes() {
        return this.contentTypes;
    }

    @Override
    public void setContentTypes(Set<MediaType> contentTypes) {
        this.contentTypes.clear();
        this.contentTypes.addAll(contentTypes);
    }
}
