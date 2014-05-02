package gov.hhs.onc.dcdt.web.filter.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.web.filter.ContentFilter;
import gov.hhs.onc.dcdt.web.media.utils.ToolMediaTypeUtils;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

public abstract class AbstractContentFilter<T extends Filter> implements ContentFilter<T> {
    protected T filter;
    protected Set<MediaType> contentTypes = new TreeSet<>(MediaType.SPECIFICITY_COMPARATOR);
    protected MutableFilterConfig filterConfig;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractContentFilter.class);

    protected AbstractContentFilter(T filter) {
        this.filter = filter;
    }

    @Override
    public void doFilter(ServletRequest servletReq, ServletResponse servletResp, FilterChain filterChain) throws IOException, ServletException {
        this.doFilterInternal(((HttpServletRequest) servletReq), ((HttpServletResponse) servletResp), filterChain);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filter.init(this.initInternal((this.filterConfig = new MutableFilterConfig(filterConfig))));

        LOGGER.debug(String.format("Initialized filter (name=%s): {%s}", this.filterConfig.getFilterName(),
            ToolStringUtils.joinDelimit(this.filterConfig.getInitParameters().entrySet(), "; ")));
    }

    @Override
    public void destroy() {
        this.filter.destroy();

        LOGGER.debug(String.format("Destroyed filter (name=%s).", this.filterConfig.getFilterName()));
    }

    protected void doFilterInternal(HttpServletRequest servletReq, HttpServletResponse servletResp, FilterChain filterChain) throws IOException,
        ServletException {
        if (this.canPreFilter(servletReq, servletResp, filterChain)) {
            this.doPreFilter(servletReq, servletResp, filterChain);

            LOGGER.trace(String.format("Pre-filtered (name=%s, class=%s) servlet request (uri=%s) and response (contentType={%s}).",
                this.filterConfig.getFilterName(), ToolClassUtils.getName(this), servletReq.getRequestURI(), servletResp.getContentType()));
        }

        filterChain.doFilter(servletReq, servletResp);

        if (this.canPostFilter(servletReq, servletResp, filterChain)) {
            this.doPostFilter(servletReq, servletResp, filterChain);

            LOGGER.trace(String.format("Post-filtered (name=%s, class=%s) servlet request (uri=%s) and response (contentType={%s}).",
                this.filterConfig.getFilterName(), ToolClassUtils.getName(this), servletReq.getRequestURI(), servletResp.getContentType()));
        }
    }

    protected void doPostFilter(HttpServletRequest servletReq, HttpServletResponse servletResp, FilterChain filterChain) throws IOException, ServletException {
        this.filter.doFilter(servletReq, servletResp, filterChain);
    }

    protected void doPreFilter(HttpServletRequest servletReq, HttpServletResponse servletResp, FilterChain filterChain) throws IOException, ServletException {
        this.filter.doFilter(servletReq, servletResp, filterChain);
    }

    protected boolean canPostFilter(HttpServletRequest servletReq, HttpServletResponse servletResp, FilterChain filterChain) {
        return this.isContentTypeIncluded(servletReq, servletResp);
    }

    protected boolean canPreFilter(HttpServletRequest servletReq, HttpServletResponse servletResp, FilterChain filterChain) {
        return false;
    }

    protected boolean isContentTypeIncluded(HttpServletRequest servletReq, HttpServletResponse servletResp) {
        String contentTypeStr = servletResp.getContentType();

        return ((contentTypeStr != null) && ToolMediaTypeUtils.isIncluded(this.contentTypes, new MediaType(MediaType.parseMediaType(contentTypeStr), null)));
    }

    protected MutableFilterConfig initInternal(MutableFilterConfig filterConfig) throws ServletException {
        return filterConfig;
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
