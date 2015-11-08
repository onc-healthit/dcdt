package gov.hhs.onc.dcdt.web.context.impl;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;

@HandlesTypes({ WebApplicationInitializer.class })
public class ToolServletContainerInitializer extends SpringServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> initClasses, ServletContext servletContext) throws ServletException {
        super.onStartup(ToolArrayUtils.asSet(ToolWebApplicationInitializer.class), servletContext);
    }
}
