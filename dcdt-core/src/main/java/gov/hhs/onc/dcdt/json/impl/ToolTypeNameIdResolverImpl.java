package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.json.ToolTypeNameIdResolver;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@Component("toolTypeNameIdResolverImpl")
@Lazy
@Scope("prototype")
public class ToolTypeNameIdResolverImpl extends AbstractToolBean implements ToolTypeNameIdResolver {
    private AbstractApplicationContext appContext;
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

        return ((valueTypeName = ToolAnnotationUtils.getValue(JsonTypeName.class, String.class, valueClass)) != null) ? valueTypeName : ToolBeanFactoryUtils
            .getBeanNameOfType(this.appContext, valueClass);
    }

    @Override
    public JavaType typeFromId(String valueId) {
        Class<?> valueBeanClass;

        for (Object valueBean : ToolBeanFactoryUtils.getBeansOfType(this.appContext, this.baseType.getRawClass())) {
            if (StringUtils.equals(valueId, ToolAnnotationUtils.getValue(JsonTypeName.class, String.class, (valueBeanClass = valueBean.getClass())))) {
                return this.baseType.narrowBy(valueBeanClass);
            }
        }

        return this.baseType;
    }

    @Override
    public Id getMechanism() {
        return Id.NAME;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
