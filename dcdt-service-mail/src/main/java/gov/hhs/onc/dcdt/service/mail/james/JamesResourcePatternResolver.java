package gov.hhs.onc.dcdt.service.mail.james;

import javax.annotation.Nullable;
import org.apache.james.container.spring.resource.JamesResourceLoader;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

public interface JamesResourcePatternResolver extends JamesResourceLoader, ResourceLoaderAware, ResourcePatternResolver {
    @Nullable
    @Override
    public Resource getResource(String resourceLoc);

    public void setAbsoluteDirectory(String absDir);

    public void setConfDirectory(String confDir);

    public void setRootDirectory(String rootDir);

    public void setVarDirectory(String varDir);
}
