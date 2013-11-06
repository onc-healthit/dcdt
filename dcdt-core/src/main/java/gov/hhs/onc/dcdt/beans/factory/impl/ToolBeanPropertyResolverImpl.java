package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.ToolBeanPropertyConversionException;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanPropertyResolver;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanPropertySource;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.core.env.AbstractPropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.util.ClassUtils;

public class ToolBeanPropertyResolverImpl extends AbstractPropertyResolver implements ToolBeanPropertyResolver {
    private final static String BEAN_PLACEHOLDER_PREFIX = "@{";

    private List<PropertySource<?>> propSources;

    {
        this.setPlaceholderPrefix(BEAN_PLACEHOLDER_PREFIX);
        this.setPlaceholderSuffix(PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX);
        this.setValueSeparator(PlaceholderConfigurerSupport.DEFAULT_VALUE_SEPARATOR);
    }

    @Override
    public boolean containsProperty(String propName) {
        if (this.hasPropertySources()) {
            for (PropertySource<?> propSource : this.propSources) {
                if (propSource.containsProperty(propName)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getProperty(String propName) {
        return this.getProperty(propName, String.class, true);
    }

    @Override
    public <T> T getProperty(String propName, Class<T> propValueClass) {
        return this.getProperty(propName, propValueClass, true);
    }

    @Override
    public <T> Class<T> getPropertyAsClass(String propName, Class<T> propValueTargetClass) {
        if (this.hasPropertySources()) {
            Object propValue;
            Class<?> propValueClass;

            for (PropertySource<?> propSource : this.propSources) {
                if (propSource.containsProperty(propName)) {
                    propValue = propSource.getProperty(propName);
                    propValueClass = propValue.getClass();

                    if (String.class.isAssignableFrom(propValueClass)) {
                        try {
                            propValue = ClassUtils.forName((String) propValue, null);
                        } catch (ClassNotFoundException e) {
                            throw new ToolBeanPropertyConversionException("Unable to get class for Spring bean property (name=" + propName + ") value: "
                                + propValue, e);
                        }
                    } else {
                        propValue = propValueClass;
                    }

                    try {
                        // noinspection ConstantConditions
                        return (Class<T>) propValue;
                    } catch (ClassCastException e) {
                        throw new ToolBeanPropertyConversionException("Unable to convert Spring bean class property (name=" + propName + ") value ("
                            + ToolClassUtils.getName(propValueClass) + ", targetClass=" + ToolClassUtils.getName(propValueTargetClass) + ").", e);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public String resolveStringValue(String strValue) {
        return this.resolvePlaceholders(strValue);
    }

    @Override
    public void setBeanPropertySources(List<ToolBeanPropertySource<?>> beanPropSources) {
        if (beanPropSources != null) {
            this.propSources = new ArrayList<>(beanPropSources.size());

            PropertySource<?> propSource;

            for (ToolBeanPropertySource<?> beanPropSource : beanPropSources) {
                if ((propSource = beanPropSource.toPropertySource()) != null) {
                    this.propSources.add(propSource);
                }
            }
        }
    }

    @Override
    protected String getPropertyAsRawString(String propName) {
        return this.getProperty(propName, String.class, false);
    }

    private <T> T getProperty(String propName, Class<T> propValueTargetClass, boolean resolveNestedPlaceholders) {
        if (this.hasPropertySources()) {
            Object propValue;
            Class<?> propValueClass;

            for (PropertySource<?> propSource : this.propSources) {
                if (propSource.containsProperty(propName)) {
                    propValue = propSource.getProperty(propName);
                    propValueClass = propValue.getClass();

                    if (String.class.isAssignableFrom(propValueClass) && resolveNestedPlaceholders) {
                        propValue = this.resolveNestedPlaceholders((String) propValue);
                    }

                    if (!this.conversionService.canConvert(propValueClass, propValueTargetClass)) {
                        throw new ToolBeanPropertyConversionException("Unable to convert Spring bean property (name=" + propName + ") value (class="
                            + ToolClassUtils.getName(propValueClass) + ", targetClass=" + ToolClassUtils.getName(propValueTargetClass) + ").");
                    }

                    return this.conversionService.convert(propValue, propValueTargetClass);
                }
            }
        }

        return null;
    }

    private boolean hasPropertySources() {
        return !ToolCollectionUtils.isEmpty(this.propSources);
    }
}
