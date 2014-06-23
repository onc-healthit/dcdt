package gov.hhs.onc.dcdt.convert.utils;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;

public abstract class ToolConversionUtils {
    public static class IsAssignableConvertiblePredicate extends AbstractToolPredicate<ConvertiblePair> {
        private Class<?> srcClass;
        private Class<?> targetClass;

        public IsAssignableConvertiblePredicate(ConvertiblePair convType) {
            this(convType.getSourceType(), convType.getTargetType());
        }

        public IsAssignableConvertiblePredicate(TypeDescriptor srcType, TypeDescriptor targetType) {
            this(srcType.getType(), targetType.getType());
        }

        public IsAssignableConvertiblePredicate(Class<?> srcClass, Class<?> targetClass) {
            this.srcClass = srcClass;
            this.targetClass = targetClass;
        }

        @Override
        protected boolean evaluateInternal(ConvertiblePair convType) throws Exception {
            return isAssignable(this.srcClass, this.targetClass, convType);
        }
    }

    public static boolean isAssignable(ConvertiblePair convType1, ConvertiblePair convType2) {
        return isAssignable(convType1.getSourceType(), convType1.getTargetType(), convType2);
    }

    public static boolean isAssignable(TypeDescriptor srcType, TypeDescriptor targetType, ConvertiblePair convType) {
        return isAssignable(srcType.getType(), targetType.getType(), convType);
    }

    @SuppressWarnings({ "unchecked" })
    public static boolean isAssignable(Class<?> srcClass, Class<?> targetClass, ConvertiblePair convType) {
        return ToolClassUtils.isAssignable(ArrayUtils.toArray(srcClass, targetClass), convType.getSourceType(), convType.getTargetType());
    }
}
