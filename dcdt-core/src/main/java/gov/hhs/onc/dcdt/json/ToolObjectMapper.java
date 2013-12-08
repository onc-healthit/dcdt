package gov.hhs.onc.dcdt.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;

public interface ToolObjectMapper extends InitializingBean {
    public Map<ConfigFeature, Boolean> getConfigFeatures();

    public void setConfigFeatures(Map<ConfigFeature, Boolean> configFeatures);

    public ObjectMapper toObjectMapper();
}
