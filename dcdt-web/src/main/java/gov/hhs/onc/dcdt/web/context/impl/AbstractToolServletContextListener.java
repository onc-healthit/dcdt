package gov.hhs.onc.dcdt.web.context.impl;

import gov.hhs.onc.dcdt.web.context.ToolServletContextListener;
import javax.servlet.ServletContextEvent;

public abstract class AbstractToolServletContextListener implements ToolServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
