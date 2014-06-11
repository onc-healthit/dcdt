package gov.hhs.onc.dcdt.context.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nullable;
import org.apache.commons.collections4.map.CompositeMap;
import org.apache.commons.collections4.map.CompositeMap.MapMutator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.Assert;

public class ToolPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {
    private static class CompositePropertySourceMutator implements MapMutator<String, Object> {
        public final static CompositePropertySourceMutator INSTANCE = new CompositePropertySourceMutator();

        private final static long serialVersionUID = 0L;

        @Override
        public void resolveCollision(CompositeMap<String, Object> sourceComp, Map<String, Object> source, Map<String, Object> sourceAdd,
            Collection<String> intersect) {
        }

        @Nullable
        @Override
        public Object put(CompositeMap<String, Object> sourceComp, Map<String, Object>[] sourceMaps, String propName, @Nullable Object propValue) {
            throw new UnsupportedOperationException(String.format("Unable to put property (name=%s, valueClass=%s) into composite property source map.",
                propName, ToolClassUtils.getName(propValue)));
        }

        @Override
        public void putAll(CompositeMap<String, Object> sourceComp, Map<String, Object>[] sourceMaps, Map<? extends String, ?> sourceMapAdd) {
            throw new UnsupportedOperationException(String.format("Unable to put all properties (num=%d) into composite property source.", sourceMapAdd.size()));
        }
    }

    private class CompositePropertySource extends EnumerablePropertySource<Map<String, Object>> {
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public CompositePropertySource(String name, Properties source) {
            super(name, ((Map) source));
        }

        @Nullable
        @Override
        public Object getProperty(String propName) {
            return this.getSource().get(propName);
        }

        @Override
        public String[] getPropertyNames() {
            return ToolCollectionUtils.toArray(this.getSource().keySet(), String.class);
        }

        @Override
        public Map<String, Object> getSource() {
            Map<String, Object> source = super.getSource();

            if (ToolPropertySourcesPlaceholderConfigurer.this.env != null) {
                Map<String, Object> sysProps = ToolPropertySourcesPlaceholderConfigurer.this.env.getSystemProperties();

                CompositeMap<String, Object> sourceComp = new CompositeMap<>();
                sourceComp.setMutator(CompositePropertySourceMutator.INSTANCE);

                if (!ToolPropertySourcesPlaceholderConfigurer.this.envOverride) {
                    sourceComp.addComposited(sysProps);
                }

                sourceComp.addComposited(source);

                if (ToolPropertySourcesPlaceholderConfigurer.this.envOverride) {
                    sourceComp.addComposited(sysProps);
                }

                source = sourceComp;
            }

            return source;
        }
    }

    private ConfigurableEnvironment env;
    private boolean envOverride;
    private PropertySources appliedPropSources;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        MutablePropertySources propSources = new MutablePropertySources();

        try {
            PropertySource<?> localPropSource = new CompositePropertySource(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, this.mergeProperties());

            if (this.localOverride) {
                propSources.addFirst(localPropSource);
            } else {
                propSources.addLast(localPropSource);
            }
        } catch (IOException e) {
            throw new BeanInitializationException("Unable to load properties.", e);
        }

        this.setPropertySources(propSources);

        this.processProperties(beanFactory, new PropertySourcesPropertyResolver(propSources));

        this.appliedPropSources = propSources;
    }

    @Override
    public PropertySources getAppliedPropertySources() throws IllegalStateException {
        Assert.state((this.appliedPropSources != null), "Property sources have not been applied yet.");

        return this.appliedPropSources;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = ((ConfigurableEnvironment) env);

        super.setEnvironment(env);
    }

    public boolean getEnvironmentOverride() {
        return this.envOverride;
    }

    public void setEnvironmentOverride(boolean envOverride) {
        this.envOverride = envOverride;
    }
}
