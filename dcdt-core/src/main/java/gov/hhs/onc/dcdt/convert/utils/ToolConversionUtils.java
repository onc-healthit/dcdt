package gov.hhs.onc.dcdt.convert.utils;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;

public abstract class ToolConversionUtils {
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
