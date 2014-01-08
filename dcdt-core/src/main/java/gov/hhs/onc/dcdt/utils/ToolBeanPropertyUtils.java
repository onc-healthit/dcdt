package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.beans.ToolBeanPropertyAccessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

public abstract class ToolBeanPropertyUtils {
    public static void copy(PropertyDescriptor beanPropDesc, Object bean1, Object bean2) throws ToolBeanPropertyAccessException {
        write(beanPropDesc, bean2, read(beanPropDesc, bean1, beanPropDesc.getPropertyType()));
    }

    public static Object read(PropertyDescriptor beanPropDesc, Object bean) throws ToolBeanPropertyAccessException {
        return read(beanPropDesc, bean, beanPropDesc.getPropertyType());
    }

    public static <T> T read(PropertyDescriptor beanPropDesc, Object bean, Class<T> beanPropValueClass) throws ToolBeanPropertyAccessException {
        if (!isReadable(beanPropDesc, null)) {
            throw new ToolBeanPropertyAccessException(toChangeEvent(beanPropDesc, bean));
        }

        try {
            Object propValue = beanPropDesc.getReadMethod().invoke(bean);

            if (propValue != null) {
                Assert.isInstanceOf(beanPropValueClass, propValue);
            }

            return beanPropValueClass.cast(propValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ToolBeanPropertyAccessException(toChangeEvent(beanPropDesc, bean), e);
        }
    }

    public static void write(PropertyDescriptor beanPropDesc, Object bean, Object beanPropValue) throws ToolBeanPropertyAccessException {
        if (!isWriteable(beanPropDesc, null)) {
            throw new ToolBeanPropertyAccessException(toChangeEvent(beanPropDesc, bean, null, beanPropValue));
        }

        try {
            beanPropDesc.getWriteMethod().invoke(bean, beanPropValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ToolBeanPropertyAccessException(toChangeEvent(beanPropDesc, bean, null, beanPropValue), e);
        }
    }

    public static boolean isReadable(PropertyDescriptor beanPropDesc, @Nullable Integer beanPropReadMods) {
        return hasAccessorMethod(beanPropDesc.getReadMethod(), beanPropReadMods);
    }

    public static boolean isWriteable(PropertyDescriptor beanPropDesc, @Nullable Integer beanPropWriteMods) {
        return hasAccessorMethod(beanPropDesc.getWriteMethod(), beanPropWriteMods);
    }

    public static boolean hasAccessorMethod(@Nullable Method accessorMethod, @Nullable Integer beanPropAccessorMods) {
        return (accessorMethod != null) && ((beanPropAccessorMods == null) || ToolMemberUtils.hasModifiers(accessorMethod, beanPropAccessorMods));
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

    public static List<AccessibleObject> getPropertyReadAccessibleObjects(PropertyDescriptor beanPropDesc) {
        List<AccessibleObject> beanPropReadAccessibleObjs = new ArrayList<>();

        if (isReadable(beanPropDesc, null)) {
            Method beanPropReadMethod = beanPropDesc.getReadMethod();
            Class<?> beanPropReadClass = beanPropReadMethod.getDeclaringClass();
            Field beanPropField = FieldUtils.getField(beanPropReadClass, beanPropDesc.getName(), true);

            if (beanPropField != null) {
                beanPropReadAccessibleObjs.add(beanPropField);
            }
            
            beanPropReadAccessibleObjs.addAll(ToolMethodUtils.getMethods(beanPropReadClass, true, beanPropReadMethod.getName(),
                beanPropReadMethod.getParameterTypes()));
        }

        return beanPropReadAccessibleObjs;
    }

    public static PropertyDescriptor describeProperty(Class<?> beanClass, String beanPropName) {
        return describeProperty(beanClass, beanPropName, null, null);
    }

    public static PropertyDescriptor describeProperty(Class<?> beanClass, String beanPropName, @Nullable Integer beanPropReadMods,
        @Nullable Integer beanPropWriteMods) {
        PropertyDescriptor beanPropDesc = BeanUtils.getPropertyDescriptor(beanClass, beanPropName);

        return ((beanPropDesc != null) && isReadable(beanPropDesc, beanPropReadMods) && isWriteable(beanPropDesc, beanPropWriteMods)) ? beanPropDesc : null;
    }

    public static List<PropertyDescriptor> describeProperties(Class<?> beanClass) {
        return describeProperties(beanClass, null, null);
    }

    public static List<PropertyDescriptor> describeProperties(Class<?> beanClass, @Nullable Integer beanPropReadMods, @Nullable Integer beanPropWriteMods) {
        List<PropertyDescriptor> beanPropDescs = ToolArrayUtils.asList(BeanUtils.getPropertyDescriptors(beanClass));
        Iterator<PropertyDescriptor> beanPropDescsIter = beanPropDescs.iterator();
        PropertyDescriptor beanPropDesc;

        while (beanPropDescsIter.hasNext() && ((beanPropDesc = beanPropDescsIter.next()) != null)) {
            if (!isReadable(beanPropDesc, beanPropReadMods) || !isWriteable(beanPropDesc, beanPropWriteMods)) {
                beanPropDescsIter.remove();
            }
        }

        return beanPropDescs;
    }
}
