package gov.hhs.onc.dcdt.service.wrapper;

import gov.hhs.onc.dcdt.net.TransportProtocol;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import gov.hhs.onc.dcdt.service.server.ToolServer;
import org.tanukisoftware.wrapper.event.WrapperEventListener;

public interface ToolServiceWrapper<T extends TransportProtocol, U extends ToolServerConfig<T>, V extends ToolServer<T, U>, W extends ToolService<T, U, V>>
    extends WrapperEventListener {
    public void stop();

    public void start();
}
