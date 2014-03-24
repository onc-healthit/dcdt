package gov.hhs.onc.dcdt.context;

import java.util.Collection;
import java.util.List;
import org.springframework.context.ApplicationContext;

public interface ToolContextLoader {
    public ApplicationContext loadContext(Collection<String> configLocs) throws Exception;

    public ApplicationContext loadContext(String ... configLocs) throws Exception;

    public String[] processLocations(String ... configLocs);

    public String[] processLocations(Class<?> clazz, String ... configLocs);

    public List<String> processLocations(Class<?> clazz, Collection<String> configLocs);

    public List<String> processLocations(Collection<String> configLocs);
}
