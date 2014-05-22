package gov.hhs.onc.dcdt.net.sockets;

import gov.hhs.onc.dcdt.beans.ToolBean;

public interface SocketRequestProcessor<T extends SocketRequest> extends ToolBean {
    public byte[] processRequest();
}
