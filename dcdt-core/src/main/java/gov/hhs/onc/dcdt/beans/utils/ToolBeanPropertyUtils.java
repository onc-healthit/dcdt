package gov.hhs.onc.dcdt.beans.utils;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanWrapper;

public abstract class ToolBeanPropertyUtils {
    public static void copy(BeanWrapper beanWrapper1, BeanWrapper beanWrapper2) {
        describeReadable(beanWrapper1).stream().map(PropertyDescriptor::getName).filter(beanPropName -> isWriteable(beanWrapper2, beanPropName)).forEach(
            beanPropName -> beanWrapper2.setPropertyValue(beanPropName, beanWrapper1.getPropertyValue(beanPropName)));
    }

    public static Collection<PropertyDescriptor> describeReadable(BeanWrapper beanWrapper) {
        return describeReadable(beanWrapper, Object.class);
    }

    public static Collection<PropertyDescriptor> describeReadable(BeanWrapper beanWrapper, Class<?> beanPropValueClass) {
        return describe(beanWrapper, beanPropValueClass, true, null);
    }

    public static Collection<PropertyDescriptor> describeWriteable(BeanWrapper beanWrapper) {
        return describeWriteable(beanWrapper, Object.class);
    }

    public static Collection<PropertyDescriptor> describeWriteable(BeanWrapper beanWrapper, Class<?> beanPropValueClass) {
        return describe(beanWrapper, beanPropValueClass, null, true);
    }

    public static Collection<PropertyDescriptor> describe(BeanWrapper beanWrapper) {
        return describe(beanWrapper, Object.class);
    }

    public static Collection<PropertyDescriptor> describe(BeanWrapper beanWrapper, Class<?> beanPropValueClass) {
        return describe(beanWrapper, beanPropValueClass, null, null);
    }

    public static Collection<PropertyDescriptor> describe(BeanWrapper beanWrapper, Class<?> beanPropValueClass, @Nullable Boolean beanPropsReadable,
        @Nullable Boolean beanPropsWriteable) {
        Predicate<PropertyDescriptor> beanPropsPredicate = null;

        if ((beanPropsReadable != null) && beanPropsReadable) {
            beanPropsPredicate = beanPropDesc -> isReadable(beanPropDesc, beanWrapper, beanPropValueClass);
        }

        if ((beanPropsWriteable != null) && beanPropsWriteable) {
            Predicate<PropertyDescriptor> beanPropDescWriteablePredicate = beanPropDesc -> isWriteable(beanPropDesc, beanWrapper,
                beanPropValueClass);

            beanPropsPredicate =
                (beanPropsPredicate != null) ? beanPropsPredicate.and(beanPropDescWriteablePredicate) : beanPropDescWriteablePredicate;
        }

        List<PropertyDescriptor> beanPropDescs = ToolArrayUtils.asList(beanWrapper.getPropertyDescriptors());

        return (beanPropsPredicate != null) ? ToolStreamUtils.filter(beanPropDescs, beanPropsPredicate) : beanPropDescs;
    }

    public static <T> boolean hasBeanPropertyValue(@Nullable Object bean, String beanPropName, @Nullable T beanPropValue) {
        Class<?> beanPropValueClass = ObjectUtils.defaultIfNull(ToolClassUtils.getClass(beanPropValue, Object.class), Object.class);

        return isReadable(ToolBeanUtils.wrap(bean), beanPropName, beanPropValueClass) && Objects.equals(getValue(ToolBeanUtils.wrap(bean), beanPropName,
            beanPropValueClass), beanPropValue);
    }

    public static boolean hasValue(@Nullable BeanWrapper beanWrapper, String beanPropName) {
        return hasValue(beanWrapper, beanPropName, Object.class);
    }

    public static <T> boolean hasValue(@Nullable BeanWrapper beanWrapper, String beanPropName, Class<T> beanPropValueClass) {
        return getValue(beanWrapper, beanPropName, beanPropValueClass) != null;
    }

    @Nullable
    public static Object getValue(@Nullable BeanWrapper beanWrapper, String beanPropName) {
        return getValue(beanWrapper, beanPropName, ((Object) null));
    }

    @Nullable
    public static Object getValue(@Nullable BeanWrapper beanWrapper, String beanPropName, @Nullable Object defaultIfNull) {
        return getValue(beanWrapper, beanPropName, Object.class, defaultIfNull);
    }

    @Nullable
    public static <T> T getValue(@Nullable BeanWrapper beanWrapper, String beanPropName, Class<T> beanPropValueClass) {
        return getValue(beanWrapper, beanPropName, beanPropValueClass, null);
    }

    @Nullable
    @SuppressWarnings({ "ConstantConditions" })
    public static <T> T getValue(@Nullable BeanWrapper beanWrapper, String beanPropName, Class<T> beanPropValueClass, @Nullable T defaultIfNull) {
        return isReadable(beanWrapper, beanPropName) ? ObjectUtils.defaultIfNull(beanPropValueClass.cast(beanWrapper.getPropertyValue(beanPropName)),
            defaultIfNull) : defaultIfNull;
    }

    @SuppressWarnings({ "ConstantConditions" })
    public static void setValue(@Nullable BeanWrapper beanWrapper, String beanPropName, @Nullable Object beanPropValue) {
        if (isWriteable(beanWrapper, beanPropName, ToolClassUtils.getClass(beanPropValue, Object.class))) {
            beanWrapper.setPropertyValue(beanPropName, beanPropValue);
        }
    }

    public static boolean isReadable(@Nullable BeanWrapper beanWrapper, String beanPropName) {
        return isReadable(beanWrapper, beanPropName, Object.class);
    }

    public static boolean isReadable(@Nullable BeanWrapper beanWrapper, String beanPropName, Class<?> beanPropValueClass) {
        return beanWrapper != null && beanWrapper.isReadableProperty(beanPropName)
            && ToolClassUtils.isAssignable(beanWrapper.getPropertyType(beanPropName), beanPropValueClass);
    }

    public static boolean isReadable(PropertyDescriptor beanPropDesc, BeanWrapper beanWrapper, @Nullable Class<?> beanPropValueClass) {
        return isReadable(beanWrapper, beanPropDesc.getName(), ObjectUtils.defaultIfNull(beanPropValueClass, Object.class));
    }

    public static boolean isWriteable(@Nullable BeanWrapper beanWrapper, String beanPropName) {
        return isWriteable(beanWrapper, beanPropName, Object.class);
    }

    public static boolean isWriteable(@Nullable BeanWrapper beanWrapper, String beanPropName, Class<?> beanPropValueClass) {
        return beanWrapper != null && beanWrapper.isWritableProperty(beanPropName)
            && ToolClassUtils.isAssignable(beanWrapper.getPropertyType(beanPropName), beanPropValueClass);
    }

    public static boolean isWriteable(PropertyDescriptor beanPropDesc, BeanWrapper beanWrapper, @Nullable Class<?> beanPropValueClass) {
        return isWriteable(beanWrapper, beanPropDesc.getName(), ObjectUtils.defaultIfNull(beanPropValueClass, Object.class));
    }
}
