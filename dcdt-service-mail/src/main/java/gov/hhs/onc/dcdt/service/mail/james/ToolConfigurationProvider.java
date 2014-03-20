package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.james.container.spring.lifecycle.ConfigurationProvider;
import org.springframework.context.ApplicationContextAware;

public interface ToolConfigurationProvider extends ApplicationContextAware, ConfigurationProvider, ToolBean {
    public String getConfigurationBuilderBeanName();

    public void setConfigurationBuilderBeanName(String configBuilderBeanName);

    public boolean hasConfigurationMappings();

    @Nullable
    public Map<String, String> getConfigurationMappings();

    public void setConfigurationMappings(@Nullable Map<String, String> configMappings);

    public ToolFileSystem getFileSystem();

    public void setFileSystem(ToolFileSystem fileSys);
}
