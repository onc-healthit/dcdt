package gov.hhs.onc.dcdt.net.sockets;

import java.net.DatagramSocket;

public interface UdpSocketListener<T extends UdpSocketAdapter, U extends SocketRequest, V extends SocketRequestProcessor<U>> extends
    SocketListener<DatagramSocket, T, DatagramSocket, T, U, V> {
}
