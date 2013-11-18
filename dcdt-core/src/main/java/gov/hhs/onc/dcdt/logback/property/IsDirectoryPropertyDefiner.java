package gov.hhs.onc.dcdt.logback.property;


import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.io.IOException;
import org.springframework.core.io.Resource;

public class IsDirectoryPropertyDefiner extends AbstractPathPropertyDefiner {
    @Override
    protected String evaluatePaths() {
        String path;
        Resource pathFirstResource;

        if (((path = super.evaluatePaths()) == null) || !(pathFirstResource = ToolListUtils.getFirst(this.pathsResourcesMap.get(path))).exists()) {
            return Boolean.toString(false);
        }

        try {
            return Boolean.toString(pathFirstResource.getFile().isDirectory());
        } catch (IOException e) {
        }

        return Boolean.toString(false);
    }
}
