package gov.hhs.onc.dcdt.version;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.util.List;

public interface ToolVersion extends ToolBean {
    public ToolModuleVersion getModuleVersion();

    public ToolModuleVersion getModuleVersion(String moduleName);

    public List<ToolModuleVersion> getModuleVersions();
}
