package gov.hhs.onc.dcdt.config.impl;


import gov.hhs.onc.dcdt.config.ToolBeanPropertySources;
import gov.hhs.onc.dcdt.config.ToolBeanPropertySourcesPropertyResolver;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.core.env.PropertySourcesPropertyResolver;

public class ToolBeanPropertySourcesPropertyResolverImpl extends PropertySourcesPropertyResolver implements ToolBeanPropertySourcesPropertyResolver
{
    private final static String BEAN_PLACEHOLDER_PREFIX = "@{";

    public ToolBeanPropertySourcesPropertyResolverImpl(ToolBeanPropertySources beanPropSources) {
        super(beanPropSources);

        this.setPlaceholderPrefix(BEAN_PLACEHOLDER_PREFIX);
        this.setPlaceholderSuffix(PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX);
        this.setValueSeparator(PlaceholderConfigurerSupport.DEFAULT_VALUE_SEPARATOR);
    }

    @Override
    public String resolveStringValue(String strValue) {
        return this.resolvePlaceholders(strValue);
    }
}
