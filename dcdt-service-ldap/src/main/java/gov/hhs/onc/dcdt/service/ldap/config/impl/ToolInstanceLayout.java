package gov.hhs.onc.dcdt.service.ldap.config.impl;

import java.nio.file.Path;
import org.apache.directory.server.core.api.InstanceLayout;

public class ToolInstanceLayout extends InstanceLayout {
    public ToolInstanceLayout(Path instanceDirPath) {
        super(instanceDirPath.toFile());
    }
}
