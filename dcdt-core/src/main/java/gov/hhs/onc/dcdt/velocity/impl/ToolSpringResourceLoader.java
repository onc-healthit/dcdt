package gov.hhs.onc.dcdt.velocity.impl;

import gov.hhs.onc.dcdt.net.utils.ToolUrlUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.ui.velocity.SpringResourceLoader;

public class ToolSpringResourceLoader extends SpringResourceLoader {
    private List<String> resourceLocPrefixes;

    @Override
    public InputStream getResourceStream(String resourceLoc) throws ResourceNotFoundException {
        ResourcePatternResolver resourcePatternResolver =
            ToolResourceUtils.getResourcePatternResolver(((ResourceLoader) this.rsvc.getApplicationAttribute(SPRING_RESOURCE_LOADER)));
        InputStream resourceInStream;

        for (String resourceLocPrefix : this.resourceLocPrefixes) {
            if ((resourceInStream = ToolResourceUtils.getInputStream(resourcePatternResolver, ToolUrlUtils.joinString(resourceLocPrefix, resourceLoc))) != null) {
                return resourceInStream;
            }
        }

        throw new ResourceNotFoundException(String.format("Unable to find resource (loc=%s) using Velocity resource loader (class=%s, locPrefixes=[%s]).",
            resourceLoc, ToolClassUtils.getName(this), ToolStringUtils.joinDelimit(this.resourceLocPrefixes, ", ")));
    }

    @Override
    public void init(ExtendedProperties configProps) {
        this.resourceLocPrefixes = ToolResourceUtils.getOverrideableLocations(((String) this.rsvc.getApplicationAttribute(SPRING_RESOURCE_LOADER_PATH)));
    }
}
