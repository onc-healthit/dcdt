package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.json.ToolTypeNameIdResolver;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.Objects;
import javax.annotation.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@Component("toolAnnoInspector")
@SuppressWarnings({ "serial" })
public class ToolAnnotationIntrospector extends JacksonAnnotationIntrospector implements ApplicationContextAware {
    private final static String ANNO_ATTR_NAME_USE = "use";

    private AbstractApplicationContext appContext;

    @Nullable
    @Override
    @SuppressWarnings({ "ConstantConditions" })
    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> mapperConfig, AnnotatedClass annotatedClassObj, JavaType baseType) {
        TypeResolverBuilder<?> typeResolverBuilder = super.findTypeResolver(mapperConfig, annotatedClassObj, baseType);
        Class<?> annotatedClass;

        if (ToolClassUtils.isAssignable(ToolClassUtils.getClass(typeResolverBuilder), StdTypeResolverBuilder.class)
            && !Objects.equals(
                ToolAnnotationUtils.getValue(JsonTypeInfo.class, Id.class, ANNO_ATTR_NAME_USE, (annotatedClass = annotatedClassObj.getAnnotated())), Id.NONE)) {
            String typePropName = ((StdTypeResolverBuilder) typeResolverBuilder).getTypeProperty();
            ToolTypeNameIdResolver typeNameIdResolver = ToolBeanFactoryUtils.createBeanOfType(this.appContext, ToolTypeNameIdResolver.class, annotatedClass);

            typeResolverBuilder.init(typeNameIdResolver.getMechanism(), typeNameIdResolver).typeProperty(typePropName);
        }

        return typeResolverBuilder;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
