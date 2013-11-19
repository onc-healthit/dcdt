package gov.hhs.onc.dcdt.version;


import gov.hhs.onc.dcdt.beans.ToolBean;
import java.util.List;

public interface ToolVersion extends ToolBean {
    public List<ToolModuleVersion> getModuleVersions();
}
