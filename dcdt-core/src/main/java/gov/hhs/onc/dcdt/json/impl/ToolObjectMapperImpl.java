package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import gov.hhs.onc.dcdt.json.ToolObjectMapper;
import gov.hhs.onc.dcdt.json.impl.JsonConfigConfiguration.JsonGeneratorConfigFeature;
import gov.hhs.onc.dcdt.json.impl.JsonConfigConfiguration.JsonParserConfigFeature;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({ "serial" })
public class ToolObjectMapperImpl extends ObjectMapper implements ToolObjectMapper {
    private final static Class<MapperFeature> CLASS_MAPPER_FEATURE = MapperFeature.class;
    private final static Class<DeserializationFeature> CLASS_DESERIALIZATION_FEATURE = DeserializationFeature.class;
    private final static Class<SerializationFeature> CLASS_SERIALIZATION_FEATURE = SerializationFeature.class;
    private final static Class<JsonParserConfigFeature> CLASS_JSON_PARSER_FEATURE = JsonParserConfigFeature.class;
    private final static Class<JsonGeneratorConfigFeature> CLASS_JSON_GEN_FEATURE = JsonGeneratorConfigFeature.class;

    private Map<ConfigFeature, Boolean> configFeatures = new HashMap<>();
    private Set<Module> modules = new HashSet<>();

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
    public Set<Module> getModules() {
        return this.modules;
    }

    @Override
    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }
}
