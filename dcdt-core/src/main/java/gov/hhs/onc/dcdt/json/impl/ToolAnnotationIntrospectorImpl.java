package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import gov.hhs.onc.dcdt.json.ToolAnnotationIntrospector;
import gov.hhs.onc.dcdt.json.ToolTypeNameIdResolver;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("toolAnnoInspectorImpl")
@Scope("singleton")
@SuppressWarnings({ "serial" })
public class ToolAnnotationIntrospectorImpl extends JacksonAnnotationIntrospector implements ToolAnnotationIntrospector {
    private ListableBeanFactory beanFactory;

    @Override
    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> mapperConfig, AnnotatedClass annotatedClass, JavaType baseType) {
        StdTypeResolverBuilder typeResolverBuilder = (StdTypeResolverBuilder) super.findTypeResolver(mapperConfig, annotatedClass, baseType);

        if (typeResolverBuilder != null) {
            String typePropName = typeResolverBuilder.getTypeProperty();
            ToolTypeNameIdResolver typeNameIdResolver = (ToolTypeNameIdResolver) this.beanFactory.getBean(
                ToolBeanFactoryUtils.getBeanNameOfType(this.beanFactory, ToolTypeNameIdResolver.class), (Object) annotatedClass.getAnnotated());

            typeResolverBuilder.init(typeNameIdResolver.getMechanism(), typeNameIdResolver).typeProperty(typePropName);
        }

        return typeResolverBuilder;
    }

    @Override
    public AnnotationIntrospector toAnnotationInspector() {
        return this;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }
}
