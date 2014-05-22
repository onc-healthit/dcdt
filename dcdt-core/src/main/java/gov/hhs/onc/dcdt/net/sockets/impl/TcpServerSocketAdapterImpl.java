package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.TcpServerSocketAdapter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import javax.annotation.Nonnegative;

public class TcpServerSocketAdapterImpl extends AbstractSocketAdapter<ServerSocket> implements TcpServerSocketAdapter {
    public TcpServerSocketAdapterImpl(ServerSocket socket) {
        super(socket);
    }

    @Override
    public Socket accept() throws IOException {
        return this.socket.accept();
    }

    @Override
    public void bind(SocketAddress bindSocketAddr) throws IOException {
        this.socket.bind(bindSocketAddr);
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
    }

    @Override
    public boolean isBound() {
        return this.socket.isBound();
    }

    @Override
    public boolean isClosed() {
        return this.socket.isClosed();
    }

    @Override
    public InetProtocol getProtocol() {
        return InetProtocol.TCP;
    }

    @Override
    public int getReceiveBufferSize() throws IOException {
        return this.socket.getReceiveBufferSize();
    }

    @Override
    public void setReceiveBufferSize(@Nonnegative int receiveBufferSize) throws IOException {
        this.socket.setReceiveBufferSize(receiveBufferSize);
    }

    @Override
    public boolean getReuseAddress() throws IOException {
        return this.socket.getReuseAddress();
    }

    @Override
    public void setReuseAddress(boolean reuseAddr) throws IOException {
        this.socket.setReuseAddress(reuseAddr);
    }

    @Override
    public ServerSocket getSocket() {
        return this.socket;
    }

    @Override
    public int getTimeout() throws IOException {
        return this.socket.getSoTimeout();
    }

    @Override
    public void setTimeout(@Nonnegative int timeout) throws IOException {
        this.socket.setSoTimeout(timeout);
    }
}
