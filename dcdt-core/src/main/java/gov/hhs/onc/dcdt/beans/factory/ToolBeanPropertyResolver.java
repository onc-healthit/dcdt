package gov.hhs.onc.dcdt.beans.factory;

import java.util.List;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.util.StringValueResolver;

public interface ToolBeanPropertyResolver extends ConfigurablePropertyResolver, StringValueResolver {
    public void setBeanPropertySources(List<ToolBeanPropertySource<?>> beanPropSources);
}
