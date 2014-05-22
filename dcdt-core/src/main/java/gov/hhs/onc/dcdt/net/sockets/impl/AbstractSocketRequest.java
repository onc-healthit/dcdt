package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.SocketRequest;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public abstract class AbstractSocketRequest extends AbstractToolBean implements SocketRequest {
    protected InetProtocol protocol;
    protected SocketAddress remoteAddr;
    protected ByteBuffer reqBuffer;
    protected ByteBuffer respBuffer;

    protected AbstractSocketRequest(InetProtocol protocol, @Nonnegative int reqBufferSize, @Nonnegative int respBufferSize) {
        this.protocol = protocol;
        this.reqBuffer = ByteBuffer.allocate(reqBufferSize);
        this.respBuffer = ByteBuffer.allocate(respBufferSize);
    }

    @Override
    public InetProtocol getProtocol() {
        return this.protocol;
    }

    @Nullable
    @Override
    public SocketAddress getRemoteAddress() {
        return this.remoteAddr;
    }

    @Override
    public void setRemoteAddress(@Nullable SocketAddress remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    @Override
    public ByteBuffer getRequestBuffer() {
        return this.reqBuffer;
    }

    @Override
    public ByteBuffer getResponseBuffer() {
        return this.respBuffer;
    }
}
