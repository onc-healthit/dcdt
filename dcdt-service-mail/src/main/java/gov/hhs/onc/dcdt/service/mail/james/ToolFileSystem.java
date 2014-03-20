package gov.hhs.onc.dcdt.service.mail.james;

import org.apache.james.filesystem.api.FileSystem;

public interface ToolFileSystem extends FileSystem {
    public JamesResourcePatternResolver getResourcePatternResolver();

    public void setResourcePatternResolver(JamesResourcePatternResolver resourcePatternResolver);
}
