package gov.hhs.onc.dcdt.net.sockets;

import java.io.IOException;
import java.net.Socket;

public interface TcpSocketAdapter extends ClientSocketAdapter<Socket> {
    public void write(byte[] data) throws IOException;
}
