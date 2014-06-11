package gov.hhs.onc.dcdt.net.sockets;

import java.net.ServerSocket;
import java.net.Socket;

public interface TcpSocketListener<T extends TcpServerSocketAdapter, U extends TcpSocketAdapter, V extends SocketRequest, W extends SocketRequestProcessor<V>>
    extends SocketListener<ServerSocket, T, Socket, U, V, W> {
}
