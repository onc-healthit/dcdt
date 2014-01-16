package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Configuration("jsonConfigConfiguration")
public class JsonConfigConfiguration {
    public static interface JsonConfigFeature<T> extends ConfigFeature {
        public T getJsonFeature();
    }

    public static interface JsonParserConfigFeature extends JsonConfigFeature<Feature> {
    }

    public static interface JsonGeneratorConfigFeature extends JsonConfigFeature<JsonGenerator.Feature> {
    }

    @Component("jsonParserConfigFeatureConv")
    @Scope("singleton")
    public static class JsonParserConfigFeatureConverter implements Converter<Feature, JsonParserConfigFeature> {
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
    public static class JsonGeneratorConfigFeatureConverter implements Converter<JsonGenerator.Feature, JsonGeneratorConfigFeature> {
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
}
