package gov.hhs.onc.dcdt.json;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;

public interface ToolObjectMapper extends InitializingBean {
    public ObjectMapper toObjectMapper();

    public Map<ConfigFeature, Boolean> getConfigFeatures();

    public void setConfigFeatures(Map<ConfigFeature, Boolean> configFeatures);

    public Set<Module> getModules();

    public void setModules(Set<Module> modules);
}
