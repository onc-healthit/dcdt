package gov.hhs.onc.dcdt.net.sockets;

import java.net.DatagramSocket;

public interface UdpSocketListener<T extends SocketRequest, U extends SocketRequestProcessor<T>> extends
    SocketListener<DatagramSocket, UdpSocketAdapter, DatagramSocket, UdpSocketAdapter, T, U> {
}
