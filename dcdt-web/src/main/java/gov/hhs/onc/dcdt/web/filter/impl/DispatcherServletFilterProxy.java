package gov.hhs.onc.dcdt.web.filter.impl;

import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.FrameworkServlet;

public class DispatcherServletFilterProxy extends DelegatingFilterProxy {
    private final static String SERVLET_NAME_DISPATCHER = "dispatcherServlet";
    private final static String CONTEXT_ATTR_NAME_DISPATCHER_WEB_APP_CONTEXT = FrameworkServlet.SERVLET_CONTEXT_PREFIX + SERVLET_NAME_DISPATCHER;

    @Override
    public String getContextAttribute() {
        return CONTEXT_ATTR_NAME_DISPATCHER_WEB_APP_CONTEXT;
    }
}
