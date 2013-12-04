package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import gov.hhs.onc.dcdt.json.ToolObjectMapper;
import gov.hhs.onc.dcdt.json.ToolSubtypeResolver;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@SuppressWarnings({ "serial" })
public class ToolObjectMapperImpl extends ObjectMapper implements ToolObjectMapper {
    private static interface JsonConfigFeature<T> extends ConfigFeature {
        public T getJsonFeature();
    }

    private static interface JsonParserConfigFeature extends JsonConfigFeature<Feature> {
    }

    private static interface JsonGeneratorConfigFeature extends JsonConfigFeature<JsonGenerator.Feature> {
    }

    @Component("jsonParserConfigFeatureConv")
    @Scope("singleton")
    private static class JsonParserConfigFeatureConverter implements Converter<Feature, JsonParserConfigFeature> {
        @Override
        public JsonParserConfigFeature convert(final Feature jsonParserFeature) {
            return new JsonParserConfigFeature() {
                @Override
                public boolean enabledByDefault() {
                    return jsonParserFeature.enabledByDefault();
                }

                @Override
                public int getMask() {
                    return jsonParserFeature.getMask();
                }

                @Override
                public Feature getJsonFeature() {
                    return jsonParserFeature;
                }
            };
        }
    }

    @Component("jsonGenConfigFeatureConv")
    @Scope("singleton")
    private static class JsonGeneratorConfigFeatureConverter implements Converter<JsonGenerator.Feature, JsonGeneratorConfigFeature> {
        @Override
        public JsonGeneratorConfigFeature convert(final JsonGenerator.Feature jsonGenFeature) {
            return new JsonGeneratorConfigFeature() {
                @Override
                public boolean enabledByDefault() {
                    return jsonGenFeature.enabledByDefault();
                }

                @Override
                public int getMask() {
                    return jsonGenFeature.getMask();
                }

                @Override
                public JsonGenerator.Feature getJsonFeature() {
                    return jsonGenFeature;
                }
            };
        }
    }

    private final static Class<MapperFeature> MAPPER_FEATURE_CLASS = MapperFeature.class;
    private final static Class<DeserializationFeature> DESERIALIZATION_FEATURE_CLASS = DeserializationFeature.class;
    private final static Class<SerializationFeature> SERIALIZATION_FEATURE_CLASS = SerializationFeature.class;
    private final static Class<JsonParserConfigFeature> JSON_PARSER_FEATURE_CLASS = JsonParserConfigFeature.class;
    private final static Class<JsonGeneratorConfigFeature> JSON_GEN_FEATURE_CLASS = JsonGeneratorConfigFeature.class;

    @Autowired
    private ToolSubtypeResolver subtypeResolver;

    private Map<ConfigFeature, Boolean> configFeatures = new HashMap<>();

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
    @SuppressWarnings({ "ConstantConditions" })
    public void afterPropertiesSet() throws Exception {
        Class<? extends ConfigFeature> configFeatureClass;
        boolean configFeatureValue;

        for (ConfigFeature configFeature : this.configFeatures.keySet()) {
            configFeatureClass = configFeature.getClass();
            configFeatureValue = this.configFeatures.get(configFeature);

            if (ToolClassUtils.isAssignable(MAPPER_FEATURE_CLASS, configFeatureClass)) {
                this.configure(MAPPER_FEATURE_CLASS.cast(configFeature), configFeatureValue);
            } else if (ToolClassUtils.isAssignable(DESERIALIZATION_FEATURE_CLASS, configFeatureClass)) {
                this.configure(DESERIALIZATION_FEATURE_CLASS.cast(configFeature), configFeatureValue);
            } else if (ToolClassUtils.isAssignable(SERIALIZATION_FEATURE_CLASS, configFeatureClass)) {
                this.configure(SERIALIZATION_FEATURE_CLASS.cast(configFeature), configFeatureValue);
            } else if (ToolClassUtils.isAssignable(JSON_PARSER_FEATURE_CLASS, configFeatureClass)) {
                this.configure(JSON_PARSER_FEATURE_CLASS.cast(configFeature).getJsonFeature(), configFeatureValue);
            } else if (ToolClassUtils.isAssignable(JSON_GEN_FEATURE_CLASS, configFeatureClass)) {
                this.configure(JSON_GEN_FEATURE_CLASS.cast(configFeature).getJsonFeature(), configFeatureValue);
            }
        }

        this.setSubtypeResolver(this.subtypeResolver.toSubtypeResolver());
    }

    @Override
    public Map<ConfigFeature, Boolean> getConfigFeatures() {
        return this.configFeatures;
    }

    @Override
    public void setConfigFeatures(Map<ConfigFeature, Boolean> configFeatures) {
        this.configFeatures.putAll(configFeatures);
    }
}
