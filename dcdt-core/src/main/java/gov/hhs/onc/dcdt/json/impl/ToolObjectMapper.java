package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import gov.hhs.onc.dcdt.convert.ToolConverter;
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

    private AnnotationIntrospector annoIntrospector;
    private Map<? extends ConfigFeature, Boolean> configFeatures = new LinkedHashMap<>();
    private Map<? extends Feature, Boolean> jsonGenFeatures = new LinkedHashMap<>();
    private Map<? extends JsonParser.Feature, Boolean> jsonParserFeatures = new LinkedHashMap<>();
    private Set<ToolConverter> convs = new LinkedHashSet<>();
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

        for (Feature jsonGenFeature : this.jsonGenFeatures.keySet()) {
            this.configure(jsonGenFeature, this.jsonGenFeatures.get(jsonGenFeature));
        }

        for (JsonParser.Feature jsonParserFeature : this.jsonParserFeatures.keySet()) {
            this.configure(jsonParserFeature, this.jsonParserFeatures.get(jsonParserFeature));
        }

        this.registerModules(this.modules);

        SimpleModule convsModule = new SimpleModule();
        JsonDeserializer<?> convJsonDeserializer;
        JsonSerializer<?> convJsonSerializer;
        Class<?> convJsonValueClass;

        for (ToolConverter conv : this.convs) {
            if (conv.canConvertJson()) {
                convsModule.addDeserializer((Class<Object>) (convJsonValueClass = (convJsonDeserializer = conv.getJsonDeserializer()).handledType()),
                    (JsonDeserializer<Object>) convJsonDeserializer);
                convsModule.addSerializer((convJsonSerializer = conv.getJsonSerializer()));

                LOGGER.debug(String.format("Added JSON deserializer and serializer for converter (class=%s, valueClass=%s, objClass=%s).",
                    ToolClassUtils.getName(this), ToolClassUtils.getName(convJsonValueClass), ToolClassUtils.getName(convJsonSerializer.handledType())));
            }
        }

        this.registerModule(convsModule);
    }

    public AnnotationIntrospector getAnnotationIntrospector() {
        return this.annoIntrospector;
    }

    @Override
    public ObjectMapper setAnnotationIntrospector(AnnotationIntrospector annoIntrospector) {
        this.annoIntrospector = annoIntrospector;

        return super.setAnnotationIntrospector(annoIntrospector);
    }

    public Map<? extends ConfigFeature, Boolean> getConfigFeatures() {
        return this.configFeatures;
    }

    public void setConfigFeatures(Map<? extends ConfigFeature, Boolean> configFeatures) {
        this.configFeatures = configFeatures;
    }

    public Set<ToolConverter> getConverters() {
        return this.convs;
    }

    public void setConverters(Set<ToolConverter> convs) {
        this.convs = convs;
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

    public Set<Module> getModules() {
        return this.modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }
}
