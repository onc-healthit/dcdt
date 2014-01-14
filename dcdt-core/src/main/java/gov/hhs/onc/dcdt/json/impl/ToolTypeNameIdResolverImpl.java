package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.json.ToolTypeNameIdResolver;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolBeanDefinitionUtils;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("toolTypeNameIdResolverImpl")
@Lazy
@Scope("prototype")
public class ToolTypeNameIdResolverImpl implements ToolTypeNameIdResolver {
    private BeanDefinitionRegistry beanDefReg;
    private ListableBeanFactory beanFactory;
    private JavaType baseType;

    public ToolTypeNameIdResolverImpl() {
        this(ToolBean.class);
    }

    public ToolTypeNameIdResolverImpl(Class<?> baseClass) {
        this.init(SimpleType.construct(baseClass));
    }

    @Override
    public void init(JavaType baseType) {
        this.baseType = baseType;
    }

    @Override
    public String idFromValue(Object valueObj) {
        return this.idFromValueAndType(valueObj, null);
    }

    @Override
    public String idFromValueAndType(Object valueObj, Class<?> valueClass) {
        return this.idFromType(ToolClassUtils.getClass(valueObj, ObjectUtils.defaultIfNull(valueClass, this.baseType.getRawClass())));
    }

    @Override
    public String idFromBaseType() {
        return this.idFromType(this.baseType.getRawClass());
    }

    @Override
    public String idFromType(Class<?> valueClass) {
        String valueTypeName;

        return ((valueTypeName = ToolAnnotationUtils.getValue(JsonTypeName.class, String.class, valueClass)) != null) ? valueTypeName : ToolBeanDefinitionUtils
            .getBeanDefinitionNameOfType(this.beanFactory, this.beanDefReg, valueClass);
    }

    @Override
    public JavaType typeFromId(String valueId) {
        Class<?> valueBeanClass;

        for (Object valueBean : ToolBeanFactoryUtils.getBeansOfType(this.beanFactory, this.baseType.getRawClass())) {
            if (StringUtils.equals(valueId, ToolAnnotationUtils.getValue(JsonTypeName.class, String.class, (valueBeanClass = valueBean.getClass())))) {
                return this.baseType.narrowBy(valueBeanClass);
            }
        }

        return this.beanDefReg.containsBeanDefinition(valueId) ? this.baseType.narrowBy(ToolBeanDefinitionUtils.getBeanDefinitionClass(this.beanDefReg,
            this.beanDefReg.getBeanDefinition(valueId))) : this.baseType;
    }

    @Override
    public Id getMechanism() {
        return Id.NAME;
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
