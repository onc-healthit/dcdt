package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import gov.hhs.onc.dcdt.json.ToolObjectMapper;
import gov.hhs.onc.dcdt.json.impl.JsonConfigConfiguration.JsonGeneratorConfigFeature;
import gov.hhs.onc.dcdt.json.impl.JsonConfigConfiguration.JsonParserConfigFeature;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;

@SuppressWarnings({ "serial" })
public class ToolObjectMapperImpl extends ObjectMapper implements ToolObjectMapper {
    private static class ConvertingJsonDeserializer<T, U> extends StdDeserializer<U> {
        private Class<T> sourceClass;
        private Class<U> targetClass;
        private TypeDescriptor sourceTypeDesc;
        private TypeDescriptor targetTypeDesc;
        private GenericConverter conv;

        public ConvertingJsonDeserializer(Class<T> sourceClass, Class<U> targetClass, GenericConverter conv) {
            super(sourceClass);

            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
            this.sourceTypeDesc = TypeDescriptor.valueOf(this.sourceClass);
            this.targetTypeDesc = TypeDescriptor.valueOf(this.targetClass);
            this.conv = conv;
        }

        @Override
        public U deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            T source = jsonParser.readValueAs(this.sourceClass);

            return (source != null) ? this.targetClass.cast(this.conv.convert(source, this.sourceTypeDesc, this.targetTypeDesc)) : null;
        }
    }

    private static class ConvertingJsonSerializer<T, U> extends StdSerializer<T> {
        private TypeDescriptor sourceTypeDesc;
        private TypeDescriptor targetTypeDesc;
        private GenericConverter conv;

        public ConvertingJsonSerializer(Class<T> sourceClass, Class<U> targetClass, GenericConverter conv) {
            super(sourceClass);

            this.sourceTypeDesc = TypeDescriptor.valueOf(sourceClass);
            this.targetTypeDesc = TypeDescriptor.valueOf(targetClass);
            this.conv = conv;
        }

        @Override
        public void serialize(T source, JsonGenerator jsonGen, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            if (source != null) {
                serializerProvider.defaultSerializeValue(this.conv.convert(source, this.sourceTypeDesc, this.targetTypeDesc), jsonGen);
            } else {
                serializerProvider.defaultSerializeNull(jsonGen);
            }
        }
    }

    private final static Class<MapperFeature> CLASS_MAPPER_FEATURE = MapperFeature.class;
    private final static Class<DeserializationFeature> CLASS_DESERIALIZATION_FEATURE = DeserializationFeature.class;
    private final static Class<SerializationFeature> CLASS_SERIALIZATION_FEATURE = SerializationFeature.class;
    private final static Class<JsonParserConfigFeature> CLASS_JSON_PARSER_FEATURE = JsonParserConfigFeature.class;
    private final static Class<JsonGeneratorConfigFeature> CLASS_JSON_GEN_FEATURE = JsonGeneratorConfigFeature.class;

    private Map<ConfigFeature, Boolean> configFeatures = new HashMap<>();
    private Set<Module> modules = new HashSet<>();
    private Map<ConvertiblePair, GenericConverter> deserializationConvs = new HashMap<>(), serializationConvs = new HashMap<>();

    public ToolObjectMapperImpl() {
        super();
    }

    public ToolObjectMapperImpl(ObjectMapper objMapper) {
        super(objMapper);
    }

    @Override
    public ObjectMapper toObjectMapper() {
        return this;
    }

    @Override
    @SuppressWarnings({ "ConstantConditions", "unchecked" })
    public void afterPropertiesSet() throws Exception {
        Class<? extends ConfigFeature> configFeatureClass;
        boolean configFeatureValue;

        for (ConfigFeature configFeature : this.configFeatures.keySet()) {
            configFeatureClass = configFeature.getClass();
            configFeatureValue = this.configFeatures.get(configFeature);

            if (ToolClassUtils.isAssignable(CLASS_MAPPER_FEATURE, configFeatureClass)) {
                this.configure(CLASS_MAPPER_FEATURE.cast(configFeature), configFeatureValue);
            } else if (ToolClassUtils.isAssignable(CLASS_DESERIALIZATION_FEATURE, configFeatureClass)) {
                this.configure(CLASS_DESERIALIZATION_FEATURE.cast(configFeature), configFeatureValue);
            } else if (ToolClassUtils.isAssignable(CLASS_SERIALIZATION_FEATURE, configFeatureClass)) {
                this.configure(CLASS_SERIALIZATION_FEATURE.cast(configFeature), configFeatureValue);
            } else if (ToolClassUtils.isAssignable(CLASS_JSON_PARSER_FEATURE, configFeatureClass)) {
                this.configure(CLASS_JSON_PARSER_FEATURE.cast(configFeature).getJsonFeature(), configFeatureValue);
            } else if (ToolClassUtils.isAssignable(CLASS_JSON_GEN_FEATURE, configFeatureClass)) {
                this.configure(CLASS_JSON_GEN_FEATURE.cast(configFeature).getJsonFeature(), configFeatureValue);
            }
        }

        this.registerModules(this.modules);

        SimpleModule convsModule = new SimpleModule();
        Class<?> convKeyClass;

        for (ConvertiblePair convPair : this.deserializationConvs.keySet()) {
            convsModule.addDeserializer((Class<Object>) (convKeyClass = convPair.getTargetType()), (JsonDeserializer<Object>) new ConvertingJsonDeserializer<>(
                convPair.getSourceType(), convKeyClass, this.deserializationConvs.get(convPair)));
        }

        for (ConvertiblePair convPair : this.serializationConvs.keySet()) {
            convsModule
                .addSerializer(new ConvertingJsonSerializer<>(convPair.getSourceType(), convPair.getTargetType(), this.serializationConvs.get(convPair)));
        }

        this.registerModule(convsModule);
    }

    @Override
    public Map<ConfigFeature, Boolean> getConfigFeatures() {
        return this.configFeatures;
    }

    @Override
    public void setConfigFeatures(Map<ConfigFeature, Boolean> configFeatures) {
        this.configFeatures = configFeatures;
    }

    @Override
    public Map<ConvertiblePair, GenericConverter> getDeserializationConverters() {
        return this.deserializationConvs;
    }

    @Override
    public void setDeserializationConverters(Map<ConvertiblePair, GenericConverter> deserializationConvs) {
        this.deserializationConvs = deserializationConvs;
    }

    @Override
    public Set<Module> getModules() {
        return this.modules;
    }

    @Override
    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    @Override
    public Map<ConvertiblePair, GenericConverter> getSerializationConverters() {
        return this.serializationConvs;
    }

    @Override
    public void setSerializationConverters(Map<ConvertiblePair, GenericConverter> serializationConvs) {
        this.serializationConvs = serializationConvs;
    }
}
