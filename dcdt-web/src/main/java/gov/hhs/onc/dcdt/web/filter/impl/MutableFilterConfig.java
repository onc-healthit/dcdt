package gov.hhs.onc.dcdt.web.filter.impl;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import org.apache.commons.collections4.EnumerationUtils;

public class MutableFilterConfig implements FilterConfig {
    private String filterName;
    private ServletContext servletContext;
    private Hashtable<String, Object> initParams = new Hashtable<>();

    public MutableFilterConfig() {
        this(null);
    }

    public MutableFilterConfig(@Nullable FilterConfig filterConfig) {
        if (filterConfig != null) {
            this.filterName = filterConfig.getFilterName();
            this.servletContext = filterConfig.getServletContext();

            EnumerationUtils.toList(filterConfig.getInitParameterNames()).forEach(initParamName ->
                this.initParams.put(initParamName, filterConfig.getInitParameter(initParamName)));
        }
    }

    @Nullable
    @Override
    public String getFilterName() {
        return this.filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    @Nullable
    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Nullable
    public String getInitParameter(String initParamName) {
        return this.getInitParameter(initParamName, null);
    }

    @Nullable
    public String getInitParameter(String initParamName, @Nullable String defaultIfNull) {
        return Objects.toString(this.initParams.get(initParamName), defaultIfNull);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return this.initParams.keys();
    }

    public Hashtable<String, Object> getInitParameters() {
        return this.initParams;
    }

    public void setInitParameters(Hashtable<String, Object> initParams) {
        this.initParams = initParams;
    }
}
