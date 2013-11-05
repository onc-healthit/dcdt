package gov.hhs.onc.dcdt.utils;


import gov.hhs.onc.dcdt.beans.ToolBeanPropertyAccessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

public abstract class ToolBeanPropertyUtils {
    public static <T> T tryRead(PropertyDescriptor beanPropDesc, Object bean, Class<T> propValueClass) {
        try {
            return read(beanPropDesc, bean, propValueClass);
        } catch (ToolBeanPropertyAccessException ignored) {
        }

        return null;
    }

    public static <T> T read(PropertyDescriptor beanPropDesc, Object bean, Class<T> beanPropValueClass) throws ToolBeanPropertyAccessException {
        if (!isReadable(beanPropDesc)) {
            throw new ToolBeanPropertyAccessException(toChangeEvent(beanPropDesc, bean));
        }

        try {
            Object propValue = getAccessibleReadMethod(beanPropDesc).invoke(bean);
            Assert.isInstanceOf(beanPropValueClass, propValue);

            return beanPropValueClass.cast(propValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ToolBeanPropertyAccessException(toChangeEvent(beanPropDesc, bean), e);
        }
    }

    public static void tryWrite(PropertyDescriptor propDesc, Object bean, Object propValue) {
        try {
            write(propDesc, bean, propValue);
        } catch (ToolBeanPropertyAccessException ignored) {
        }
    }

    public static void write(PropertyDescriptor beanPropDesc, Object bean, Object beanPropValue) throws ToolBeanPropertyAccessException {
        if (!isWriteable(beanPropDesc)) {
            throw new ToolBeanPropertyAccessException(toChangeEvent(beanPropDesc, bean, null, beanPropValue));
        }

        try {
            getAccessibleWriteMethod(beanPropDesc).invoke(bean, beanPropValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ToolBeanPropertyAccessException(toChangeEvent(beanPropDesc, bean, null, beanPropValue), e);
        }
    }

    public static boolean isReadable(PropertyDescriptor beanPropDesc) {
        return getAccessibleReadMethod(beanPropDesc) != null;
    }

    public static boolean isWriteable(PropertyDescriptor beanPropDesc) {
        return getAccessibleWriteMethod(beanPropDesc) != null;
    }

    public static Method getAccessibleReadMethod(PropertyDescriptor beanPropDesc) {
        return MethodUtils.getAccessibleMethod(beanPropDesc.getReadMethod());
    }

    public static Method getAccessibleWriteMethod(PropertyDescriptor beanPropDesc) {
        return MethodUtils.getAccessibleMethod(beanPropDesc.getWriteMethod());
    }

    public static PropertyChangeEvent toChangeEvent(PropertyDescriptor beanPropDesc) {
        return toChangeEvent(beanPropDesc, null);
    }

    public static PropertyChangeEvent toChangeEvent(PropertyDescriptor beanPropDesc, Object bean) {
        return toChangeEvent(beanPropDesc, bean, null, null);
    }

    public static PropertyChangeEvent toChangeEvent(PropertyDescriptor beanPropDesc, Object bean, Object oldBeanPropValue, Object newBeanPropValue) {
        return new PropertyChangeEvent(bean, beanPropDesc.getName(), oldBeanPropValue, newBeanPropValue);
    }

    public static PropertyDescriptor describeProperty(Class<?> beanClass, String beanPropName) {
        return describeProperty(beanClass, beanPropName, null, null);
    }

    public static PropertyDescriptor describeProperty(Class<?> beanClass, String beanPropName, Boolean beanPropReadable, Boolean beanPropWriteable) {
        PropertyDescriptor beanPropDesc = BeanUtils.getPropertyDescriptor(beanClass, beanPropName);

        return ((beanPropDesc != null) && ((beanPropReadable == null) || isReadable(beanPropDesc)) && ((beanPropWriteable == null) || isWriteable(beanPropDesc))) ? beanPropDesc
                : null;
    }

    public static List<PropertyDescriptor> describeProperties(Class<?> beanClass) {
        return describeProperties(beanClass, null, null);
    }

    public static List<PropertyDescriptor> describeProperties(Class<?> beanClass, Boolean beanPropReadable, Boolean beanPropWriteable) {
        List<PropertyDescriptor> beanPropDescs = new ArrayList<>(Arrays.asList(BeanUtils.getPropertyDescriptors(beanClass)));
        Iterator<PropertyDescriptor> beanPropDescsIter = beanPropDescs.iterator();
        PropertyDescriptor beanPropDesc;

        while(beanPropDescsIter.hasNext() && ((beanPropDesc = beanPropDescsIter.next()) != null)) {
            if (((beanPropReadable != null) && !isReadable(beanPropDesc)) || ((beanPropWriteable != null) && !isWriteable(beanPropDesc))) {
                beanPropDescsIter.remove();
            }
        }

        return beanPropDescs;
    }
}
