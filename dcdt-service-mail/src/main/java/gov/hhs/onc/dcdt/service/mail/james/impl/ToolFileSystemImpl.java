package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.service.mail.james.JamesResourcePatternResolver;
import gov.hhs.onc.dcdt.service.mail.james.ToolFileSystem;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ToolFileSystemImpl implements ToolFileSystem {
    private JamesResourcePatternResolver resourcePatternResolver;

    @Override
    public InputStream getResource(String resourceLoc) throws IOException {
        InputStream resourceInStream = ToolResourceUtils.getInputStream(this.resourcePatternResolver, resourceLoc);

        if (resourceInStream == null) {
            throw new FileNotFoundException(String.format("Unable to get resource (loc=%s) input stream.", resourceLoc));
        }

        return resourceInStream;
    }

    @Override
    public File getFile(String resourceLoc) throws FileNotFoundException {
        File resourceFile = ToolResourceUtils.getFile(this.resourcePatternResolver, resourceLoc);

        if (resourceFile == null) {
            throw new FileNotFoundException(String.format("Unable to get resource (loc=%s) file.", resourceLoc));
        }

        return resourceFile;
    }

    @Override
    public File getBasedir() throws FileNotFoundException {
        return new File(this.resourcePatternResolver.getRootDirectory());
    }

    @Override
    public JamesResourcePatternResolver getResourcePatternResolver() {
        return this.resourcePatternResolver;
    }
    
    @Override
    public void setResourcePatternResolver(JamesResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }
}
