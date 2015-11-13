package gov.hhs.onc.dcdt.web.context.impl;

import gov.hhs.onc.dcdt.context.impl.AbstractToolContextLoader;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class WebContextLoader extends AbstractToolContextLoader<XmlWebApplicationContext> {
    private static class ToolContextLoaderListener extends ContextLoaderListener {
        public ToolContextLoaderListener(XmlWebApplicationContext appContext) {
            super(appContext);
        }

        @Override
        public void contextInitialized(ServletContextEvent event) {
        }
    }

    private ServletContext servletContext;

    public WebContextLoader(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected void initializeContext() throws Exception {
        appContext.setServletContext(this.servletContext);

        super.initializeContext();
    }

    @Override
    protected void buildContext(String ... configLocs) throws Exception {
        (this.appContext = new XmlWebApplicationContext()).setConfigLocations(configLocs);

        this.servletContext.addListener(new ToolContextLoaderListener(this.appContext));
    }
}
