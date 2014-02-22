package gov.hhs.onc.dcdt.context.impl;

import gov.hhs.onc.dcdt.context.ToolContextLoader;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.Collection;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ToolContextLoaderImpl implements ToolContextLoader {
    @Override
    public ApplicationContext loadContext(Collection<String> configLocs) throws Exception {
        return this.loadContext(ToolCollectionUtils.toArray(configLocs, String.class));
    }

    @Override
    public ApplicationContext loadContext(String ... configLocs) throws Exception {
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(configLocs);
        appContext.start();

        return appContext;
    }

    @Override
    public String[] processLocations(String ... configLocs) {
        return this.processLocations(this.getClass(), configLocs);
    }

    @Override
    public String[] processLocations(Class<?> clazz, String ... configLocs) {
        return ToolCollectionUtils.toArray(this.processLocations(clazz, ToolArrayUtils.asList(configLocs)), String.class);
    }

    @Override
    public List<String> processLocations(Collection<String> configLocs) {
        return this.processLocations(this.getClass(), configLocs);
    }

    @Override
    public List<String> processLocations(Class<?> clazz, Collection<String> configLocs) {
        return ToolResourceUtils.getOverrideableResourceLocations(configLocs);
    }
}
