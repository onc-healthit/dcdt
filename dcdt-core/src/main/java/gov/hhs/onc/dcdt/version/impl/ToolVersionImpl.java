package gov.hhs.onc.dcdt.version.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.version.ToolModuleVersion;
import gov.hhs.onc.dcdt.version.ToolVersion;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("toolVersionImpl")
public class ToolVersionImpl extends AbstractToolBean implements ToolVersion {
    private final static String MODULE_NAME_DEFAULT = "dcdt-parent";

    @Autowired
    private List<ToolModuleVersion> moduleVersions;

    @Override
    public ToolModuleVersion getModuleVersion() {
        return this.getModuleVersion(MODULE_NAME_DEFAULT);
    }

    @Override
    public ToolModuleVersion getModuleVersion(String moduleName) {
        for (ToolModuleVersion moduleVersion : this.moduleVersions) {
            if (StringUtils.equals(moduleVersion.getName(), moduleName)) {
                return moduleVersion;
            }
        }

        return null;
    }

    @Override
    public List<ToolModuleVersion> getModuleVersions() {
        return this.moduleVersions;
    }
}
