package gov.hhs.onc.dcdt.convert.impl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.convert.ConversionRuntimeException;
import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.ConvertsUserType;
import gov.hhs.onc.dcdt.convert.ToolConverter;
import gov.hhs.onc.dcdt.data.types.ToolUserType;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.TypeDescriptor;

public abstract class AbstractToolConverter extends AbstractToolBean implements ToolConverter {
    @SuppressWarnings({ "serial" })
    protected static class ConvertingJsonDeserializer<T, U> extends StdDeserializer<U> {
        private ToolConverter conv;
        private Class<T> sourceClass;
        private Class<U> targetClass;

        public ConvertingJsonDeserializer(ToolConverter conv, Class<T> sourceClass, Class<U> targetClass) {
            super(targetClass);

            this.conv = conv;
            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
        }

        @Nullable
        @Override
        public U deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return this.conv.convert(jsonParser.readValueAs(this.sourceClass), this.sourceClass, this.targetClass);
        }
    }

    @SuppressWarnings({ "serial" })
    protected static class ConvertingJsonSerializer<T, U> extends StdSerializer<T> {
        private ToolConverter conv;
        private Class<T> sourceClass;
        private Class<U> targetClass;

        public ConvertingJsonSerializer(ToolConverter conv, Class<T> sourceClass, Class<U> targetClass) {
            super(sourceClass);

            this.conv = conv;
            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
        }

        @Override
        public void serialize(T source, JsonGenerator jsonGen, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            serializerProvider.defaultSerializeValue(this.conv.convert(source, this.sourceClass, this.targetClass), jsonGen);
        }
    }

    protected final static TypeDescriptor TYPE_DESC_BYTE = TypeDescriptor.valueOf(byte.class);
    protected final static TypeDescriptor TYPE_DESC_BYTE_ARR = TypeDescriptor.array(TYPE_DESC_BYTE);
    protected final static TypeDescriptor TYPE_DESC_STR = TypeDescriptor.valueOf(String.class);
    protected final static TypeDescriptor TYPE_DESC_STR_ARR = TypeDescriptor.array(TYPE_DESC_STR);

    protected Set<ConvertiblePair> convertPairs;
    protected JsonDeserializer<?> jsonDeserializer;
    protected JsonSerializer<?> jsonSerializer;
    protected Class<? extends ToolUserType<?, ?, ?, ?>> userTypeClass;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolConverter.class);

    @Nullable
    @Override
    public <T, U> U convert(@Nullable T source, Class<T> sourceClass, Class<U> targetClass) {
        return targetClass.cast(this.convert(source, TypeDescriptor.valueOf(sourceClass), TypeDescriptor.valueOf(targetClass)));
    }

    @Nullable
    @Override
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }

        ConvertiblePair convertPair = this.findConvertibleTypes(sourceType, targetType);

        try {
            Object target = this.convertInternal(source, sourceType, targetType, convertPair);

            LOGGER.trace(String.format("Converted (class=%s) source object (class=%s, sourceClass=%s) to target object (class=%s, targetClass=%s).",
                ToolClassUtils.getName(this), ToolClassUtils.getName(source), ToolClassUtils.getName(sourceType), ToolClassUtils.getName(target),
                ToolClassUtils.getName(targetType)));

            return target;
        } catch (Exception e) {
            throw new ConversionRuntimeException(String.format(
                "Unable to convert (class=%s) source object (class=%s, sourceClass=%s) to target (targetClass=%s).", ToolClassUtils.getName(this),
                ToolClassUtils.getName(source), ToolClassUtils.getName(sourceType), ToolClassUtils.getName(targetType)), e);
        }
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return this.findConvertibleTypes(sourceType, targetType) != null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Class<?> convClass = this.getClass();

        this.convertPairs =
            toConvertiblePairs(ToolCollectionUtils.addAll(
                ToolArrayUtils.unwrapElements(ToolAnnotationUtils.getValues(Converts.List.class, Converts[].class, convClass)),
                ToolAnnotationUtils.findAnnotations(Converts.class, convClass)));

        ConvertiblePair jsonConvertPair;

        if ((jsonConvertPair =
            toConvertiblePair(ToolAnnotationUtils.getValue(ConvertsJson.class, Converts.class, ConvertsJson.ANNO_ATTR_NAME_DESERIALIZE, convClass))) != null) {
            this.jsonDeserializer = new ConvertingJsonDeserializer<>(this, jsonConvertPair.getSourceType(), jsonConvertPair.getTargetType());
        }

        if ((jsonConvertPair =
            toConvertiblePair(ToolAnnotationUtils.getValue(ConvertsJson.class, Converts.class, ConvertsJson.ANNO_ATTR_NAME_SERIALIZE, convClass))) != null) {
            this.jsonSerializer = new ConvertingJsonSerializer<>(this, jsonConvertPair.getSourceType(), jsonConvertPair.getTargetType());
        }

        ConvertsUserType convertsUserTypeAnno = ToolAnnotationUtils.findAnnotation(ConvertsUserType.class, convClass);
        this.userTypeClass = (convertsUserTypeAnno != null) ? convertsUserTypeAnno.value() : null;
    }

    protected static Set<ConvertiblePair> toConvertiblePairs(List<Converts> convertsAnnos) {
        Set<ConvertiblePair> convertPairs = new LinkedHashSet<>(convertsAnnos.size());

        for (Converts convertsAnno : convertsAnnos) {
            convertPairs.add(toConvertiblePair(convertsAnno));
        }

        return convertPairs;
    }

    @Nullable
    protected static ConvertiblePair toConvertiblePair(@Nullable Converts convertsAnno) {
        return (convertsAnno != null) ? new ConvertiblePair(convertsAnno.from(), convertsAnno.to()) : null;
    }

    @Nullable
    protected abstract Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair)
        throws Exception;

    @Nullable
    protected ConvertiblePair findConvertibleTypes(TypeDescriptor sourceType, TypeDescriptor targetType) {
        for (ConvertiblePair convertPair : this.convertPairs) {
            if (sourceType.isAssignableTo(TypeDescriptor.valueOf(convertPair.getSourceType()))
                && targetType.isAssignableTo(TypeDescriptor.valueOf(convertPair.getTargetType()))) {
                return convertPair;
            }
        }

        return null;
    }

    @Override
    public boolean canConvertJson() {
        return this.hasJsonDeserializer() && this.hasJsonSerializer();
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return this.convertPairs;
    }

    @Override
    public boolean hasJsonDeserializer() {
        return this.jsonDeserializer != null;
    }

    @Nullable
    @Override
    public JsonDeserializer<?> getJsonDeserializer() {
        return this.jsonDeserializer;
    }

    @Override
    public boolean hasJsonSerializer() {
        return this.jsonSerializer != null;
    }

    @Nullable
    @Override
    public JsonSerializer<?> getJsonSerializer() {
        return this.jsonSerializer;
    }

    @Override
    public boolean hasUserTypeClass() {
        return this.userTypeClass != null;
    }

    @Nullable
    @Override
    public Class<? extends ToolUserType<?, ?, ?, ?>> getUserTypeClass() {
        return this.userTypeClass;
    }
}
