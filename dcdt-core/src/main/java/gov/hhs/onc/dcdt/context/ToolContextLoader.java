package gov.hhs.onc.dcdt.context;

import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

public interface ToolContextLoader<T extends AbstractRefreshableConfigApplicationContext> {
    public T loadContext(String ... configLocs) throws Exception;

    public String[] processLocations(String ... configLocs);

    public String[] processLocations(Class<?> clazz, String ... configLocs);
}
