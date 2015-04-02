package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

@SuppressWarnings({ "serial" })
public class ToolObjectMapper extends ObjectMapper implements InitializingBean {
    private final static Class<MapperFeature> CLASS_MAPPER_FEATURE = MapperFeature.class;
    private final static Class<DeserializationFeature> CLASS_DESERIALIZATION_FEATURE = DeserializationFeature.class;
    private final static Class<SerializationFeature> CLASS_SERIALIZATION_FEATURE = SerializationFeature.class;

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolObjectMapper.class);

    private Map<? extends ConfigFeature, Boolean> configFeatures = new LinkedHashMap<>();
    private Map<? extends Feature, Boolean> jsonGenFeatures = new LinkedHashMap<>();
    private Map<? extends JsonParser.Feature, Boolean> jsonParserFeatures = new LinkedHashMap<>();
    private Set<JsonDeserializer<?>> jsonDeserializers = new LinkedHashSet<>();
    private Set<JsonSerializer<?>> jsonSerializers = new LinkedHashSet<>();
    private Set<Module> modules = new LinkedHashSet<>();

    public ToolObjectMapper() {
        super();
    }

    public ToolObjectMapper(ObjectMapper objMapper) {
        super(objMapper);
    }

    @Override
    @SuppressWarnings({ "ConstantConditions", "unchecked" })
    public void afterPropertiesSet() throws Exception {
        Class<? extends ConfigFeature> configFeatureClass;

        for (ConfigFeature configFeature : this.configFeatures.keySet()) {
            if (ToolClassUtils.isAssignable(CLASS_MAPPER_FEATURE, (configFeatureClass = configFeature.getClass()))) {
                this.configure(CLASS_MAPPER_FEATURE.cast(configFeature), this.configFeatures.get(configFeature));
            } else if (ToolClassUtils.isAssignable(CLASS_DESERIALIZATION_FEATURE, configFeatureClass)) {
                this.configure(CLASS_DESERIALIZATION_FEATURE.cast(configFeature), this.configFeatures.get(configFeature));
            } else if (ToolClassUtils.isAssignable(CLASS_SERIALIZATION_FEATURE, configFeatureClass)) {
                this.configure(CLASS_SERIALIZATION_FEATURE.cast(configFeature), this.configFeatures.get(configFeature));
            }
        }

        this.jsonGenFeatures.keySet().forEach(jsonGenFeature -> this.configure(jsonGenFeature, this.jsonGenFeatures.get(jsonGenFeature)));

        this.jsonParserFeatures.keySet().forEach(jsonParserFeature -> this.configure(jsonParserFeature, this.jsonParserFeatures.get(jsonParserFeature)));

        this.registerModules(this.modules);

        SimpleModule convsModule = new SimpleModule();

        for (JsonDeserializer<?> jsonDeserializer : this.jsonDeserializers) {
            // noinspection RedundantCast
            convsModule.addDeserializer(((Class<Object>) jsonDeserializer.handledType()), ((JsonDeserializer<Object>) jsonDeserializer));

            LOGGER.debug(String.format("Added JSON deserializer (class=%s, valueClass=%s).", ToolClassUtils.getName(jsonDeserializer),
                ToolClassUtils.getName(jsonDeserializer.handledType())));
        }

        for (JsonSerializer<?> jsonSerializer : this.jsonSerializers) {
            convsModule.addSerializer(jsonSerializer);

            LOGGER.debug(String.format("Added JSON serializer (class=%s, objClass=%s).", ToolClassUtils.getName(jsonSerializer),
                ToolClassUtils.getName(jsonSerializer.handledType())));
        }

        this.registerModule(convsModule);
    }

    public Map<? extends ConfigFeature, Boolean> getConfigFeatures() {
        return this.configFeatures;
    }

    public void setConfigFeatures(Map<? extends ConfigFeature, Boolean> configFeatures) {
        this.configFeatures = configFeatures;
    }

    public Set<JsonDeserializer<?>> getJsonDeserializers() {
        return this.jsonDeserializers;
    }

    public void setJsonDeserializers(Set<JsonDeserializer<?>> jsonDeserializers) {
        this.jsonDeserializers = jsonDeserializers;
    }

    public Map<? extends Feature, Boolean> getJsonGeneratorFeatures() {
        return this.jsonGenFeatures;
    }

    public void setJsonGeneratorFeatures(Map<? extends Feature, Boolean> jsonGenFeatures) {
        this.jsonGenFeatures = jsonGenFeatures;
    }

    public Map<? extends JsonParser.Feature, Boolean> getJsonParserFeatures() {
        return this.jsonParserFeatures;
    }

    public void setJsonParserFeatures(Map<? extends JsonParser.Feature, Boolean> jsonParserFeatures) {
        this.jsonParserFeatures = jsonParserFeatures;
    }

    public Set<JsonSerializer<?>> getJsonSerializers() {
        return this.jsonSerializers;
    }

    public void setJsonSerializers(Set<JsonSerializer<?>> jsonSerializers) {
        this.jsonSerializers = jsonSerializers;
    }

    public Set<Module> getModules() {
        return this.modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }
}
