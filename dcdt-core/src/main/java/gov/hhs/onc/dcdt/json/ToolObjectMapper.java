package gov.hhs.onc.dcdt.json;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;

public interface ToolObjectMapper extends InitializingBean {
    public ObjectMapper toObjectMapper();

    public Map<ConfigFeature, Boolean> getConfigFeatures();

    public void setConfigFeatures(Map<ConfigFeature, Boolean> configFeatures);

    public Map<ConvertiblePair, GenericConverter> getDeserializationConverters();

    public void setDeserializationConverters(Map<ConvertiblePair, GenericConverter> deserializationConvs);

    public Set<Module> getModules();

    public void setModules(Set<Module> modules);

    public Map<ConvertiblePair, GenericConverter> getSerializationConverters();

    public void setSerializationConverters(Map<ConvertiblePair, GenericConverter> serializationConvs);
}
