package gov.hhs.onc.dcdt.service.wrapper;

import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import gov.hhs.onc.dcdt.service.server.ToolServer;
import org.tanukisoftware.wrapper.event.WrapperEventListener;

public interface ToolServiceWrapper<T extends ToolServerConfig, U extends ToolServer<T>, V extends ToolService<T, U>> extends WrapperEventListener {
    public void stop();

    public void start();
}
