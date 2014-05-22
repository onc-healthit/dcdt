package gov.hhs.onc.dcdt.net.sockets;

import java.net.ServerSocket;
import java.net.Socket;

public interface TcpSocketListener<T extends SocketRequest, U extends SocketRequestProcessor<T>> extends
    SocketListener<ServerSocket, TcpServerSocketAdapter, Socket, TcpSocketAdapter, T, U> {
}
