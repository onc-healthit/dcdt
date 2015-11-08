package gov.hhs.onc.dcdt.web.context.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class ToolWebApplicationInitializer implements WebApplicationInitializer {
    private final static String[] CONTEXT_CONFIG_LOCS = ArrayUtils.toArray("spring/spring-core*.xml", "spring/spring-service*.xml", "spring/spring-web*.xml");

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebContextLoader contextLoader = new WebContextLoader(servletContext);
        String[] configLocs = contextLoader.processLocations(CONTEXT_CONFIG_LOCS);
        XmlWebApplicationContext appContext;

        try {
            appContext = contextLoader.loadContext(configLocs);
        } catch (Exception e) {
            throw new ServletException(String.format("Unable to load (configLocs=[%s]) Spring web application context.", StringUtils.join(configLocs, ", ")), e);
        }

        List<ServletContextInitializer> servletContextInits = ToolBeanFactoryUtils.getBeansOfType(appContext.getBeanFactory(), ServletContextInitializer.class);

        if (servletContextInits.isEmpty()) {
            return;
        }

        servletContextInits.sort(AnnotationAwareOrderComparator.INSTANCE);

        for (ServletContextInitializer servletContextInit : servletContextInits) {
            servletContextInit.onStartup(servletContext);
        }
    }
}
