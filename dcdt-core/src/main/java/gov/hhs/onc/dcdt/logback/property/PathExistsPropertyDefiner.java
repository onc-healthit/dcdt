package gov.hhs.onc.dcdt.logback.property;

import gov.hhs.onc.dcdt.utils.ToolListUtils;

public class PathExistsPropertyDefiner extends AbstractPathPropertyDefiner {
    @Override
    protected String evaluatePaths() {
        String path;

        return Boolean.toString(((path = super.evaluatePaths()) != null) && ToolListUtils.getFirst(this.pathsResourcesMap.get(path)).exists());
    }
}
