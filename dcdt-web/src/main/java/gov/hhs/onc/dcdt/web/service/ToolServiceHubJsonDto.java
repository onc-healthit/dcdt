package gov.hhs.onc.dcdt.web.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.web.service.impl.ToolServiceHubJsonDtoImpl;
import java.util.List;
import java.util.Map;

@JsonSubTypes({ @Type(ToolServiceHubJsonDtoImpl.class) })
public interface ToolServiceHubJsonDto extends ToolBeanJsonDto<ToolServiceHub> {
    @JsonProperty("serviceMap")
    public Map<ToolServiceType, ToolService<?, ?>> getServiceMap();

    public void setServiceMap(Map<ToolServiceType, ToolService<?, ?>> serviceMap);

    @JsonProperty("serviceMsgsMap")
    public Map<ToolServiceType, List<String>> getServiceMessagesMap();

    public void setServiceMessagesMap(Map<ToolServiceType, List<String>> serviceMsgsMap);
}
