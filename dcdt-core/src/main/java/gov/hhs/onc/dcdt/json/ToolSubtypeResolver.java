package gov.hhs.onc.dcdt.json;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import gov.hhs.onc.dcdt.beans.factory.BeanDefinitionRegistryAware;
import java.util.Collection;
import org.springframework.beans.factory.BeanFactoryAware;

public interface ToolSubtypeResolver extends BeanDefinitionRegistryAware, BeanFactoryAware {
    public void registerSubtypes(NamedType ... types);

    public void registerSubtypes(Class<?> ... classes);

    public Collection<NamedType> collectAndResolveSubtypes(AnnotatedMember annoMember, MapperConfig<?> mapperConfig, AnnotationIntrospector annoInspector,
        JavaType baseType);

    public Collection<NamedType> collectAndResolveSubtypes(AnnotatedClass annoClass, MapperConfig<?> mapperConfig, AnnotationIntrospector annoInspector);

    public SubtypeResolver toSubtypeResolver();
}
