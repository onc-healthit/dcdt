package gov.hhs.onc.dcdt.beans.utils;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanWrapper;

public abstract class ToolBeanPropertyUtils {
    public static class BeanPropertyValueTransformer<T> extends AbstractToolTransformer<Object, T> {
        private String beanPropName;
        private Class<T> beanPropValueClass;

        public BeanPropertyValueTransformer(String beanPropName, Class<T> beanPropValueClass) {
            this.beanPropName = beanPropName;
            this.beanPropValueClass = beanPropValueClass;
        }

        @Nullable
        @Override
        protected T transformInternal(@Nullable Object bean) throws Exception {
            return getValue(ToolBeanUtils.wrap(bean), this.beanPropName, this.beanPropValueClass);
        }
    }

    public static class BeanPropertyReadablePredicate extends AbstractNamedBeanPropertyPredicate<Object> {
        public BeanPropertyReadablePredicate(String beanPropName) {
            this(beanPropName, null);
        }

        public BeanPropertyReadablePredicate(String beanPropName, @Nullable Class<?> beanPropValueClass) {
            super(beanPropName, beanPropValueClass);
        }

        @Override
        protected boolean evaluateInternal(@Nullable Object bean) throws Exception {
            return isReadable(ToolBeanUtils.wrap(bean), this.beanPropName, beanPropValueClass);
        }
    }

    public static class BeanPropertyWriteablePredicate extends AbstractNamedBeanPropertyPredicate<Object> {
        public BeanPropertyWriteablePredicate(String beanPropName) {
            this(beanPropName, null);
        }

        public BeanPropertyWriteablePredicate(String beanPropName, @Nullable Class<?> beanPropValueClass) {
            super(beanPropName, beanPropValueClass);
        }

        @Override
        protected boolean evaluateInternal(@Nullable Object bean) throws Exception {
            return isWriteable(ToolBeanUtils.wrap(bean), this.beanPropName, beanPropValueClass);
        }
    }

    public static class BeanPropertyDescriptorReadablePredicate extends AbstractBeanPropertyDescriptorPredicate {
        public BeanPropertyDescriptorReadablePredicate(BeanWrapper beanWrapper) {
            this(beanWrapper, null);
        }

        public BeanPropertyDescriptorReadablePredicate(BeanWrapper beanWrapper, @Nullable Class<?> beanPropValueClass) {
            super(beanWrapper, beanPropValueClass);
        }

        @Override
        protected boolean evaluateInternal(PropertyDescriptor beanPropDesc) throws Exception {
            return isReadable(this.beanWrapper, beanPropDesc.getName(), this.beanPropValueClass);
        }
    }

    public static class BeanPropertyDescriptorWriteablePredicate extends AbstractBeanPropertyDescriptorPredicate {
        public BeanPropertyDescriptorWriteablePredicate(BeanWrapper beanWrapper) {
            this(beanWrapper, null);
        }

        public BeanPropertyDescriptorWriteablePredicate(BeanWrapper beanWrapper, @Nullable Class<?> beanPropValueClass) {
            super(beanWrapper, beanPropValueClass);
        }

        @Override
        protected boolean evaluateInternal(PropertyDescriptor beanPropDesc) throws Exception {
            return isWriteable(this.beanWrapper, beanPropDesc.getName(), this.beanPropValueClass);
        }
    }

    private static abstract class AbstractBeanPropertyDescriptorPredicate extends AbstractBeanPropertyPredicate<PropertyDescriptor> {
        protected BeanWrapper beanWrapper;

        protected AbstractBeanPropertyDescriptorPredicate(BeanWrapper beanWrapper) {
            this(beanWrapper, null);
        }

        protected AbstractBeanPropertyDescriptorPredicate(BeanWrapper beanWrapper, @Nullable Class<?> beanPropValueClass) {
            super(beanPropValueClass);

            this.beanWrapper = beanWrapper;
        }
    }

    private static abstract class AbstractNamedBeanPropertyPredicate<T> extends AbstractBeanPropertyPredicate<T> {
        protected String beanPropName;

        protected AbstractNamedBeanPropertyPredicate(String beanPropName) {
            this(beanPropName, null);
        }

        protected AbstractNamedBeanPropertyPredicate(String beanPropName, @Nullable Class<?> beanPropValueClass) {
            super(beanPropValueClass);

            this.beanPropName = beanPropName;
        }
    }

    private static abstract class AbstractBeanPropertyPredicate<T> extends AbstractToolPredicate<T> {
        protected Class<?> beanPropValueClass;

        protected AbstractBeanPropertyPredicate() {
            this(null);
        }

        protected AbstractBeanPropertyPredicate(@Nullable Class<?> beanPropValueClass) {
            this.beanPropValueClass = ObjectUtils.defaultIfNull(beanPropValueClass, Object.class);
        }
    }

    public static void copy(BeanWrapper beanWrapper1, BeanWrapper beanWrapper2) {
        String beanPropName;

        for (PropertyDescriptor beanPropDesc : describeReadable(beanWrapper1)) {
            if (isWriteable(beanWrapper2, (beanPropName = beanPropDesc.getName()))) {
                beanWrapper2.setPropertyValue(beanPropName, beanWrapper1.getPropertyValue(beanPropName));
            }
        }
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

    @SuppressWarnings({ "unchecked" })
    public static Collection<PropertyDescriptor> describe(BeanWrapper beanWrapper, Class<?> beanPropValueClass, @Nullable Boolean beanPropsReadable,
        @Nullable Boolean beanPropsWriteable) {
        Predicate<PropertyDescriptor> beanPropsPredicate = null;

        if ((beanPropsReadable != null) && beanPropsReadable) {
            beanPropsPredicate = new BeanPropertyDescriptorReadablePredicate(beanWrapper, beanPropValueClass);
        }

        if ((beanPropsWriteable != null) && beanPropsWriteable) {
            Predicate<PropertyDescriptor> beanPropDescWriteablePredicate = new BeanPropertyDescriptorWriteablePredicate(beanWrapper, beanPropValueClass);

            beanPropsPredicate =
                (beanPropsPredicate != null) ? PredicateUtils.allPredicate(beanPropsPredicate, beanPropDescWriteablePredicate) : beanPropDescWriteablePredicate;
        }

        List<PropertyDescriptor> beanPropDescs = ToolArrayUtils.asList(beanWrapper.getPropertyDescriptors());

        return (beanPropsPredicate != null) ? CollectionUtils.select(beanPropDescs, beanPropsPredicate) : beanPropDescs;
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

    public static boolean isWriteable(@Nullable BeanWrapper beanWrapper, String beanPropName) {
        return isWriteable(beanWrapper, beanPropName, Object.class);
    }

    public static boolean isWriteable(@Nullable BeanWrapper beanWrapper, String beanPropName, Class<?> beanPropValueClass) {
        return beanWrapper != null && beanWrapper.isWritableProperty(beanPropName)
            && ToolClassUtils.isAssignable(beanWrapper.getPropertyType(beanPropName), beanPropValueClass);
    }
}
