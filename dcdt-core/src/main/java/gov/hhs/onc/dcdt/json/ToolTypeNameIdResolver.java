package gov.hhs.onc.dcdt.json;

import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import gov.hhs.onc.dcdt.beans.factory.BeanDefinitionRegistryAware;
import org.springframework.beans.factory.BeanFactoryAware;

public interface ToolTypeNameIdResolver extends BeanDefinitionRegistryAware, BeanFactoryAware, TypeIdResolver {
    public String idFromType(Class<?> clazz);
}
