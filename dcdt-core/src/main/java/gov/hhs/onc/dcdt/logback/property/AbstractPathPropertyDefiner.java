package gov.hhs.onc.dcdt.logback.property;

import ch.qos.logback.core.PropertyDefinerBase;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

public abstract class AbstractPathPropertyDefiner extends PropertyDefinerBase {
    protected String pathsStr;
    protected List<String> paths;
    protected Map<String, List<Resource>> pathsResourcesMap;

    @Override
    public String getPropertyValue() {
        return this.evaluatePaths();
    }

    protected String evaluatePaths() {
        this.resolvePathsResources();

        return !MapUtils.isEmpty(this.pathsResourcesMap) ? ToolListUtils.getFirst(new ArrayList<>(this.pathsResourcesMap.keySet())) : null;
    }

    protected void resolvePathsResources() {
        if (StringUtils.isBlank(this.pathsStr)) {
            this.addError("A path must be provided.");

            return;
        }

        this.paths = ToolResourceUtils.splitResourceLocations(this.pathsStr);
        this.pathsResourcesMap = ToolResourceUtils.mapResourceLocations(this.paths);
    }

    public String getPaths() {
        return this.pathsStr;
    }

    public void setPaths(String pathsStr) {
        this.pathsStr = pathsStr;
    }
}
