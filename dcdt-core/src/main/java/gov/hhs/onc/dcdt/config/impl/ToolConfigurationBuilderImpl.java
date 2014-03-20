package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.config.ToolConfigurationBuilder;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.interpol.ConfigurationInterpolator;
import org.apache.commons.lang.text.StrLookup;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.AbstractFileResolvingResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

public class ToolConfigurationBuilderImpl extends DefaultConfigurationBuilder implements ToolConfigurationBuilder {
    private final static long serialVersionUID = 0L;

    private AbstractApplicationContext appContext;
    private List<Resource> configResources;
    private Map<String, StrLookup> localLookups;
    private boolean enableSubstitutionInVars;
    private boolean forceReloadCheck;

    public ToolConfigurationBuilderImpl() {
        this(((List<Resource>) null));
    }

    public ToolConfigurationBuilderImpl(@Nullable Resource ... configResources) {
        this(ToolArrayUtils.asList(configResources));
    }

    public ToolConfigurationBuilderImpl(@Nullable List<Resource> configResources) {
        super();

        this.configResources = configResources;
    }

    @Override
    public CombinedConfiguration getObject() throws Exception {
        return this.getConfiguration();
    }

    @Override
    public CombinedConfiguration getConfiguration() throws ConfigurationException {
        return this.getConfiguration(false);
    }

    @Override
    public CombinedConfiguration getConfiguration(boolean load) throws ConfigurationException {
        CombinedConfiguration combinedConfig = super.getConfiguration(load);
        combinedConfig.setForceReloadCheck(this.forceReloadCheck);

        final ConfigurableEnvironment env = this.appContext.getEnvironment();

        ConfigurationInterpolator interpolator = new ConfigurationInterpolator();
        interpolator.setDefaultLookup(new StrLookup() {
            @Nullable
            @Override
            public String lookup(String key) {
                return env.getProperty(key);
            }
        });

        combinedConfig.getInterpolator().setParentInterpolator(interpolator);
        combinedConfig.getSubstitutor().setEnableSubstitutionInVariables(this.enableSubstitutionInVars);

        if (this.hasConfigResources()) {
            Class<? extends Resource> configResourceClass;
            XMLConfiguration config;

            for (Resource configResource : this.configResources) {
                if (!configResource.exists()) {
                    continue;
                }

                config = new XMLConfiguration();
                configResourceClass = configResource.getClass();

                try {
                    if (ToolClassUtils.isAssignable(configResourceClass, ClassPathResource.class)
                        || ToolClassUtils.isAssignable(configResourceClass, InputStreamResource.class)) {
                        config.load(configResource.getInputStream());
                    } else if (ToolClassUtils.isAssignable(configResourceClass, AbstractFileResolvingResource.class)) {
                        config.load(configResource.getFile());
                    } else {
                        config.load(configResource.getURL());
                    }
                } catch (IOException e) {
                    throw new ConfigurationException(String.format("Unable to load XML configuration from resource (class=%s).",
                        ToolClassUtils.getName(configResourceClass)), e);
                }

                config.getInterpolator().setParentInterpolator(interpolator);
                config.getSubstitutor().setEnableSubstitutionInVariables(this.enableSubstitutionInVars);

                combinedConfig.addConfiguration(config);
            }
        }
        
        return combinedConfig;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public boolean hasConfigResources() {
        return !CollectionUtils.isEmpty(this.configResources);
    }

    @Nullable
    @Override
    public List<Resource> getConfigResources() {
        return this.configResources;
    }

    @Override
    public void setConfigResources(@Nullable List<Resource> configResources) {
        this.configResources = configResources;
    }

    @Override
    public boolean isEagerInit() {
        return false;
    }

    @Override
    public boolean isEnableSubstitutionInVariables() {
        return this.enableSubstitutionInVars;
    }

    @Override
    public void setEnableSubstitutionInVariables(boolean enableSubstitutionInVars) {
        this.enableSubstitutionInVars = enableSubstitutionInVars;
    }

    @Override
    public boolean isForceReloadCheck() {
        return this.forceReloadCheck;
    }

    @Override
    public void setForceReloadCheck(boolean forceReloadCheck) {
        this.forceReloadCheck = forceReloadCheck;
    }

    @Override
    public boolean hasLocalLookups() {
        return !MapUtils.isEmpty(this.localLookups);
    }

    @Nullable
    @Override
    public Map<String, StrLookup> getLocalLookups() {
        return this.localLookups;
    }

    @Override
    public void setLocalLookups(@Nullable Map<String, StrLookup> localLookups) {
        this.localLookups = localLookups;
    }

    @Override
    public Class<?> getObjectType() {
        return CombinedConfiguration.class;
    }

    @Override
    public boolean isPrototype() {
        return true;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
