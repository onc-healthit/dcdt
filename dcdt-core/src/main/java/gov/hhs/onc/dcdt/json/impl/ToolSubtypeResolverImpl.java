package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import gov.hhs.onc.dcdt.json.ToolSubtypeResolver;
import gov.hhs.onc.dcdt.utils.ToolBeanDefinitionUtils;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("toolSubtypeResolverImpl")
@Scope("singleton")
@SuppressWarnings({ "serial" })
public class ToolSubtypeResolverImpl extends StdSubtypeResolver implements ToolSubtypeResolver {
    private BeanDefinitionRegistry beanDefReg;
    private ListableBeanFactory beanFactory;

    @Override
    public SubtypeResolver toSubtypeResolver() {
        return this;
    }

    @Override
    public Collection<NamedType> collectAndResolveSubtypes(AnnotatedMember annoMember, MapperConfig<?> mapperConfig, AnnotationIntrospector annoInspector,
        JavaType baseType) {
        return this.collectAndResolveSubtypes(super.collectAndResolveSubtypes(annoMember, mapperConfig, annoInspector, baseType), ((baseType == null)
            ? annoMember.getRawType() : baseType.getRawClass()));
    }

    @Override
    public Collection<NamedType> collectAndResolveSubtypes(AnnotatedClass annoClass, MapperConfig<?> mapperConfig, AnnotationIntrospector annoInspector) {
        return this.collectAndResolveSubtypes(super.collectAndResolveSubtypes(annoClass, mapperConfig, annoInspector), annoClass.getRawType());
    }

    protected Collection<NamedType> collectAndResolveSubtypes(Collection<NamedType> subtypes, Class<?> clazz) {
        Set<NamedType> subtypesNamed = new LinkedHashSet<>(subtypes);
        
        for (Class<?> subtypeBeanClass : ToolBeanDefinitionUtils.getBeanDefinitionClassesOfType(this.beanFactory, this.beanDefReg, clazz, true)) {
            subtypesNamed.add(new NamedType(subtypeBeanClass));
        }
        
        return subtypesNamed;
    }

    @Override
    public void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefReg) {
        this.beanDefReg = beanDefReg;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }
}
