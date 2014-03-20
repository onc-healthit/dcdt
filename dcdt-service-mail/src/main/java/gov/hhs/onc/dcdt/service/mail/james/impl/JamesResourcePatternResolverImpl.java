package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.service.mail.james.JamesResourcePatternResolver;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.io.IOException;
import javax.annotation.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

public class JamesResourcePatternResolverImpl implements JamesResourcePatternResolver {
    private ResourcePatternResolver resourcePatternResolver;
    private String absDir;
    private String confDir;
    private String rootDir;
    private String varDir;

    @Nullable
    @Override
    public Resource getResource(String resourceLoc) {
        return ToolResourceUtils.resolveLocation(this.resourcePatternResolver, resourceLoc);
    }

    @Override
    public Resource[] getResources(String resourceLoc) throws IOException {
        return this.resourcePatternResolver.getResources(resourceLoc);
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.resourcePatternResolver.getClassLoader();
    }

    @Override
    public String getAbsoluteDirectory() {
        return this.absDir;
    }

    @Override
    public void setAbsoluteDirectory(String absDir) {
        this.absDir = absDir;
    }

    @Override
    public String getConfDirectory() {
        return this.confDir;
    }

    @Override
    public void setConfDirectory(String confDir) {
        this.confDir = confDir;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ToolResourceUtils.getResourcePatternResolver(resourceLoader);
    }

    @Override
    public String getRootDirectory() {
        return this.rootDir;
    }

    @Override
    public void setRootDirectory(String rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public String getVarDirectory() {
        return this.varDir;
    }

    @Override
    public void setVarDirectory(String varDir) {
        this.varDir = varDir;
    }
}
