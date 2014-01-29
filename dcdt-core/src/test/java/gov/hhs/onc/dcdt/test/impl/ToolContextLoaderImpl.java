package gov.hhs.onc.dcdt.test.impl;

import gov.hhs.onc.dcdt.test.ToolContextLoader;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ToolContextLoaderImpl implements ToolContextLoader {
    @Override
    public ApplicationContext loadContext(String ... configLocs) throws Exception {
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(configLocs);
        appContext.start();

        return appContext;
    }

    @Override
    public String[] processLocations(Class<?> clazz, String ... configLocs) {
        List<String> overrideableConfigLocs = ToolResourceUtils.getOverrideableResourceLocations(ToolArrayUtils.asList(configLocs));

        return overrideableConfigLocs.toArray(new String[overrideableConfigLocs.size()]);
    }
}
