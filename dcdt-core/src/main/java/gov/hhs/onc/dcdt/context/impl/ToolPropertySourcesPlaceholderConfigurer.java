package gov.hhs.onc.dcdt.context.impl;

import java.io.IOException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

public class ToolPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {
    private Environment env;
    private boolean envOverride;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        MutablePropertySources propSources = new MutablePropertySources();

        try {
            PropertySource<?> localPropSource = new PropertiesPropertySource(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, this.mergeProperties());

            if (this.localOverride) {
                propSources.addFirst(localPropSource);
            } else {
                propSources.addLast(localPropSource);
            }
        } catch (IOException e) {
            throw new BeanInitializationException("Unable to load properties.", e);
        }
        
        if (this.env != null) {
            PropertySource<Environment> envPropSource = new PropertySource<Environment>(ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME, this.env) {
                @Override
                public Object getProperty(String propName) {
                    return this.source.getProperty(propName);
                }
            };

            if (this.envOverride) {
                propSources.addFirst(envPropSource);
            } else {
                propSources.addLast(envPropSource);
            }
        }
        
        this.setPropertySources(propSources);

        super.postProcessBeanFactory(beanFactory);
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;

        super.setEnvironment(env);
    }

    public boolean getEnvironmentOverride() {
        return this.envOverride;
    }
    
    public void setEnvironmentOverride(boolean envOverride) {
        this.envOverride = envOverride;
    }
}
