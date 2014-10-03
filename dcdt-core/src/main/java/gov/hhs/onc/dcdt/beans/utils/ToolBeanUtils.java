package gov.hhs.onc.dcdt.beans.utils;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanPropertyUtils.BeanPropertyValueTransformer;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.persistence.Id;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.convert.ConversionService;

public abstract class ToolBeanUtils {
    public final static String PROP_NAME_NAME = "name";

    @Nullable
    public static Serializable getId(BeanWrapper beanWrapper) {
        for (PropertyDescriptor beanPropDesc : ToolBeanPropertyUtils.describeReadable(beanWrapper)) {
            if (ToolAnnotationUtils.findAnnotation(Id.class, beanPropDesc.getReadMethod()) != null) {
                return ToolBeanPropertyUtils.getValue(beanWrapper, beanPropDesc.getName(), Serializable.class);
            }
        }

        return null;
    }

    @Nullable
    public static ToolNamedBean findNamed(ListableBeanFactory beanFactory, String namedBeanName) {
        return findNamed(beanFactory, namedBeanName, ToolNamedBean.class);
    }

    @Nullable
    public static <T extends ToolNamedBean> T findNamed(ListableBeanFactory beanFactory, String namedBeanName, Class<T> namedBeanClass) {
        return CollectionUtils.find(
            ToolBeanFactoryUtils.getBeansOfType(beanFactory, namedBeanClass),
            PredicateUtils.transformedPredicate(new BeanPropertyValueTransformer<>(PROP_NAME_NAME, Object.class),
                PredicateUtils.equalPredicate(((Object) namedBeanName))));
    }

    @Nullable
    public static BeanWrapper wrap(@Nullable Object bean) {
        return wrap(bean, null);
    }

    @Nullable
    public static BeanWrapper wrap(@Nullable Object bean, @Nullable ConversionService convService) {
        if (bean != null) {
            BeanWrapper beanWrapper = (bean instanceof BeanWrapper) ? ((BeanWrapper) bean) : new BeanWrapperImpl(bean);

            if (convService != null) {
                beanWrapper.setConversionService(convService);
            }

            return beanWrapper;
        } else {
            return null;
        }
    }
}
