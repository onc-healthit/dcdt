package gov.hhs.onc.dcdt.net.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface TcpServerSocketAdapter extends SocketAdapter<ServerSocket> {
    public Socket accept() throws IOException;
}
