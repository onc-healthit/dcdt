package gov.hhs.onc.dcdt.web.utils;

import gov.hhs.onc.dcdt.web.view.ViewDirectiveType;
import java.util.EnumSet;
import org.apache.commons.lang3.StringUtils;

public abstract class ToolViewUtils {
    public static String overrideDirective(String viewName, ViewDirectiveType viewDirType) {
        return containsDirective(viewName)
            ? (viewDirType.getViewNamePrefix() + StringUtils.removeStart(viewName, getDirective(viewName).getViewNamePrefix())) : viewName;
    }
    
    public static boolean containsDirective(String viewName) {
        return getDirective(viewName).isDirective();
    }

    public static ViewDirectiveType getDirective(String viewName) {
        for (ViewDirectiveType viewDirType : EnumSet.allOf(ViewDirectiveType.class)) {
            if (viewDirType.isDirective() && StringUtils.startsWith(viewName, viewDirType.getViewNamePrefix())) {
                return viewDirType;
            }
        }

        return ViewDirectiveType.NONE;
    }
}
