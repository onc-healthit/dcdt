package gov.hhs.onc.dcdt.web.service;

import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import gov.hhs.onc.dcdt.service.ToolService;
import java.util.List;
import java.util.Map;

public interface ToolServiceHub extends ToolLifecycleBean {
    public Map<ToolServiceType, ToolService<?, ?>> getServiceMap();

    public Map<ToolServiceType, List<String>> getServiceMessagesMap();
}
