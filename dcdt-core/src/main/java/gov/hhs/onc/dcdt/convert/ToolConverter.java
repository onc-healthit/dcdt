package gov.hhs.onc.dcdt.convert;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.types.ToolUserType;
import java.util.Set;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

public interface ToolConverter extends ConditionalGenericConverter, ToolBean {
    @Nullable
    public <T, U> U convert(@Nullable T source, Class<T> sourceClass, Class<U> targetClass);

    @Nullable
    @Override
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType);

    public boolean hasJsonDeserializers();

    public Set<JsonDeserializer<?>> getJsonDeserializers();

    public boolean hasJsonSerializers();

    public Set<JsonSerializer<?>> getJsonSerializers();

    public boolean hasUserTypeClass();

    @Nullable
    public Class<? extends ToolUserType<?, ?, ?, ?>> getUserTypeClass();
}
