package gov.hhs.onc.dcdt.convert;

import gov.hhs.onc.dcdt.beans.ToolBean;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

public interface ToolConverter extends ConditionalGenericConverter, ToolBean {
    @Nullable
    public <T, U> U convert(@Nullable T src, Class<T> srcClass, Class<U> targetClass);

    public boolean canConvert(@Nullable Object src, TypeDescriptor srcType, TypeDescriptor targetType, @Nullable ConvertiblePair convType);

    @Nullable
    public ConvertiblePair findConvertibleType(@Nullable Object src, TypeDescriptor srcType, TypeDescriptor targetType);
}
