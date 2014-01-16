package gov.hhs.onc.dcdt.convert;

import gov.hhs.onc.dcdt.beans.ToolBean;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

public interface ToolConverter extends ConditionalGenericConverter, ToolBean {
    @Nullable
    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
}
