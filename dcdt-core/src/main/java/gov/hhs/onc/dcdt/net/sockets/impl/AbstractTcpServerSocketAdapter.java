package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.SocketAdapterOptions;
import gov.hhs.onc.dcdt.net.sockets.TcpServerSocketAdapter;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public abstract class AbstractTcpServerSocketAdapter extends AbstractSocketAdapter<ServerSocket> implements TcpServerSocketAdapter {
    protected int backlog;

    protected AbstractTcpServerSocketAdapter(ServerSocket socket) {
        super(socket);
    }

    @Override
    public Socket accept() throws IOException {
        return this.socket.accept();
    }

    @Override
    public void bind(SocketAddress bindSocketAddr) throws IOException {
        if (this.hasBacklog()) {
            this.socket.bind(bindSocketAddr, this.backlog);
        } else {
            this.socket.bind(bindSocketAddr);
        }
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
    }

    @Override
    protected <U> void setOptionInternal(SocketOption<U> optKey, U optValue) throws IOException {
        if (optKey == SocketAdapterOptions.BACKLOG) {
            this.setBacklog(((Integer) optValue));
        } else {
            super.setOptionInternal(optKey, optValue);
        }
    }

    @Override
    public boolean hasBacklog() {
        return ToolNumberUtils.isPositive(this.backlog);
    }

    @Override
    public int getBacklog() {
        return this.backlog;
    }

    @Override
    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    @Override
    public boolean isBound() {
        return this.socket.isBound();
    }

    @Override
    public boolean isClosed() {
        return this.socket.isClosed();
    }

    @Nullable
    @Override
    public SocketAddress getLocalSocketAddress() {
        return this.socket.getLocalSocketAddress();
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
