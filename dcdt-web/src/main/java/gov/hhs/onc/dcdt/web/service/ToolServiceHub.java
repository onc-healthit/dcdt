package gov.hhs.onc.dcdt.web.service;

import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import gov.hhs.onc.dcdt.service.ToolService;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContextAware;

public interface ToolServiceHub extends ApplicationContextAware, ToolLifecycleBean {
    public Map<ToolServiceType, ToolService<?, ?>> getServiceMap();

    public Map<ToolServiceType, List<String>> getServiceMessagesMap();
}
