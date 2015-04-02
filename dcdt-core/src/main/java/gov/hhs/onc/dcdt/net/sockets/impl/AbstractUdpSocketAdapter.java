package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.UdpSocketAdapter;
import gov.hhs.onc.dcdt.net.sockets.utils.ToolSocketUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractUdpSocketAdapter extends AbstractClientSocketAdapter<DatagramSocket> implements UdpSocketAdapter {
    protected AbstractUdpSocketAdapter(DatagramSocket socket) {
        super(socket);
    }

    @Override
    public void write(byte[] data, SocketAddress remoteAddr) throws IOException {
        this.socket.send(ToolSocketUtils.createPacket(data, remoteAddr));
    }

    @Override
    public Pair<SocketAddress, byte[]> read() throws IOException {
        return this.read(this.getProtocol().getDataSizeMax());
    }

    @Override
    public Pair<SocketAddress, byte[]> read(@Nonnegative int bufferLen) throws IOException {
        return this.read(new byte[bufferLen]);
    }

    @Override
    public Pair<SocketAddress, byte[]> read(byte[] buffer) throws IOException {
        DatagramPacket packet = ToolSocketUtils.createPacket(buffer);

        this.socket.receive(packet);

        return new MutablePair<>(packet.getSocketAddress(), packet.getData());
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

    @Nullable
    @Override
    public SocketAddress getLocalSocketAddress() {
        return this.socket.getLocalSocketAddress();
    }

    @Override
    public InetProtocol getProtocol() {
        return InetProtocol.UDP;
    }

    @Override
    public int getReceiveBufferSize() throws IOException {
        return this.socket.getReceiveBufferSize();
    }

    @Override
    public void setReceiveBufferSize(@Nonnegative int receiveBufferSize) throws IOException {
        this.socket.setReceiveBufferSize(receiveBufferSize);
    }

    @Nullable
    @Override
    public SocketAddress getRemoteSocketAddress() {
        return this.socket.getRemoteSocketAddress();
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
    public int getSendBufferSize() throws IOException {
        return this.socket.getSendBufferSize();
    }

    @Override
    public void setSendBufferSize(@Nonnegative int sendBufferSize) throws IOException {
        this.socket.setSendBufferSize(sendBufferSize);
    }

    @Override
    public DatagramSocket getSocket() {
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
