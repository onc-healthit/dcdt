package gov.hhs.onc.dcdt.web.filter.impl;

import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.lang3.StringUtils;
import org.w3c.tidy.servlet.Consts;
import org.w3c.tidy.servlet.filter.BufferedServletOutputStream;
import org.w3c.tidy.servlet.filter.BufferedServletResponse;
import org.w3c.tidy.servlet.filter.JTidyFilter;

public class ToolJTidyFilter extends AbstractContentFilter<JTidyFilter> {
    private class JTidyFilterChain implements FilterChain {
        private FilterChain filterChain;

        public JTidyFilterChain(FilterChain filterChain) {
            this.filterChain = filterChain;
        }

        @Override
        public void doFilter(ServletRequest servletReq, ServletResponse servletResp) throws IOException, ServletException {
            this.filterChain.doFilter(servletReq, new HttpServletResponseWrapper(((HttpServletResponse) servletResp)) {
                @Override
                public void setContentType(String contentTypeStr) {
                    if (ToolJTidyFilter.this.isContentTypeIncluded(contentTypeStr)) {
                        super.setContentType(contentTypeStr);
                    } else {
                        BufferedServletResponse bufferedServletResp = ((BufferedServletResponse) this.getResponse());
                        bufferedServletResp.getResponse().setContentType(contentTypeStr);

                        try {
                            ((BufferedServletOutputStream) bufferedServletResp.getOutputStream()).setBinary(true);
                        } catch (IOException ignored) {
                        }
                    }
                }
            });

            if (!ToolJTidyFilter.this.isContentTypeIncluded(servletResp.getContentType())) {
                servletReq.setAttribute(Consts.ATTRIBUTE_IGNORE, true);
            }
        }
    }

    private final static String DELIM_CONFIG_ENTRY = "; ";
    private final static String DELIM_CONFIG_ENTRY_PAIR = ": ";

    private Map<String, Object> config = new LinkedHashMap<>();

    public ToolJTidyFilter() {
        super(new JTidyFilter());
    }

    @Override
    protected void doPreFilter(HttpServletRequest servletReq, HttpServletResponse servletResp, FilterChain filterChain) throws IOException, ServletException {
        super.doPreFilter(servletReq, servletResp, new JTidyFilterChain(filterChain));
    }

    @Override
    protected boolean canPreFilter(HttpServletRequest servletReq, HttpServletResponse servletResp, FilterChain filterChain) {
        return true;
    }

    @Override
    protected MutableFilterConfig initInternal(MutableFilterConfig filterConfig) throws ServletException {
        ToolStrBuilder configStrBuilder = new ToolStrBuilder();

        for (String configPropName : this.config.keySet()) {
            configStrBuilder.appendDelimiter(DELIM_CONFIG_ENTRY);
            configStrBuilder.append(configPropName);
            configStrBuilder.appendDelimiter(DELIM_CONFIG_ENTRY_PAIR);
            configStrBuilder.append(Objects.toString(this.config.get(configPropName), StringUtils.EMPTY));
        }

        filterConfig.getInitParameters().put(JTidyFilter.CONFIG_CONFIG, configStrBuilder.build());

        return super.initInternal(filterConfig);
    }

    public Map<String, Object> getConfig() {
        return this.config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config.clear();
        this.config.putAll(config);
    }
}
