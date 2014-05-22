package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.TcpSocketAdapter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import javax.annotation.Nonnegative;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class TcpSocketAdapterImpl extends AbstractClientSocketAdapter<Socket> implements TcpSocketAdapter {
    public TcpSocketAdapterImpl(Socket socket) {
        super(socket);
    }

    @Override
    public void write(byte[] data) throws IOException {
        this.write(data, this.socket.getRemoteSocketAddress());
    }

    @Override
    public void write(byte[] data, SocketAddress remoteAddr) throws IOException {
        OutputStream outStream = this.socket.getOutputStream();

        IOUtils.write(data, outStream);

        outStream.flush();
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
        int dataLen = IOUtils.read(this.socket.getInputStream(), buffer);

        return new MutablePair<>(this.socket.getRemoteSocketAddress(), ArrayUtils.subarray(buffer, 0, dataLen));
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
    public int getSendBufferSize() throws IOException {
        return this.socket.getSendBufferSize();
    }

    @Override
    public void setSendBufferSize(@Nonnegative int sendBufferSize) throws IOException {
        this.socket.setSendBufferSize(sendBufferSize);
    }

    @Override
    public Socket getSocket() {
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
