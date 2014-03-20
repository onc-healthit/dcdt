package gov.hhs.onc.dcdt.config;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationBuilder;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.text.StrLookup;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

public interface ToolConfigurationBuilder extends ApplicationContextAware, ConfigurationBuilder, SmartFactoryBean<CombinedConfiguration> {
    @Override
    public CombinedConfiguration getConfiguration() throws ConfigurationException;

    public boolean hasConfigResources();

    @Nullable
    public List<Resource> getConfigResources();

    public void setConfigResources(@Nullable List<Resource> configResources);

    public boolean isDelimiterParsingDisabled();

    public void setDelimiterParsingDisabled(boolean delimParsingDisabled);

    public boolean isEnableSubstitutionInVariables();

    public void setEnableSubstitutionInVariables(boolean enableSubstitutionInVars);

    public boolean isForceReloadCheck();

    public void setForceReloadCheck(boolean forceReloadCheck);

    public boolean hasLocalLookups();

    @Nullable
    public Map<String, StrLookup> getLocalLookups();

    public void setLocalLookups(@Nullable Map<String, StrLookup> localLookups);
}
