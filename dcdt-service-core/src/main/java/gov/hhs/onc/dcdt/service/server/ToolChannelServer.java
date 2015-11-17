package gov.hhs.onc.dcdt.service.server;

import gov.hhs.onc.dcdt.net.TransportProtocol;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;

public interface ToolChannelServer<T extends TransportProtocol, U extends ToolServerConfig<T>> extends ToolServer<T, U> {
}
