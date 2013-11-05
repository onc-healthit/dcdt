package gov.hhs.onc.dcdt.config;


import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.util.StringValueResolver;

public interface ToolBeanPropertySourcesPropertyResolver extends ConfigurablePropertyResolver, StringValueResolver {
}
