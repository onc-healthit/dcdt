package gov.hhs.onc.dcdt.version.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.version.ToolModuleVersion;
import gov.hhs.onc.dcdt.version.ToolVersion;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("toolVersionImpl")
@Scope("singleton")
public class ToolVersionImpl extends AbstractToolBean implements ToolVersion {
    @Autowired
    private List<ToolModuleVersion> moduleVersions;

    @Override
    public List<ToolModuleVersion> getModuleVersions() {
        return this.moduleVersions;
    }
}
