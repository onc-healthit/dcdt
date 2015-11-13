package gov.hhs.onc.dcdt.web.service.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.web.service.ToolServiceHub;
import gov.hhs.onc.dcdt.web.service.ToolServiceHubJsonDto;
import gov.hhs.onc.dcdt.web.service.ToolServiceType;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("toolServiceHubJsonDtoImpl")
@JsonTypeName("toolServiceHub")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class ToolServiceHubJsonDtoImpl extends AbstractToolBeanJsonDto<ToolServiceHub> implements ToolServiceHubJsonDto {
    private Map<ToolServiceType, ToolService<?, ?>> serviceMap;
    private Map<ToolServiceType, List<String>> serviceMsgsMap;

    public ToolServiceHubJsonDtoImpl() {
        super(ToolServiceHub.class, ToolServiceHubImpl.class);
    }

    @Override
    public Map<ToolServiceType, ToolService<?, ?>> getServiceMap() {
        return this.serviceMap;
    }

    @Override
    public void setServiceMap(Map<ToolServiceType, ToolService<?, ?>> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public Map<ToolServiceType, List<String>> getServiceMessagesMap() {
        return this.serviceMsgsMap;
    }

    @Override
    public void setServiceMessagesMap(Map<ToolServiceType, List<String>> serviceMsgsMap) {
        this.serviceMsgsMap = serviceMsgsMap;
    }
}
