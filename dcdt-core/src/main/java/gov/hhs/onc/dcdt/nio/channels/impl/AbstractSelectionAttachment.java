package gov.hhs.onc.dcdt.nio.channels.impl;

import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.nio.channels.SelectionAttachment;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractSelectionAttachment implements SelectionAttachment {
    protected InetProtocol protocol;
    protected ByteBuffer reqBuffer;
    protected ByteBuffer respBuffer;
    protected SocketAddress socketAddr;

    protected AbstractSelectionAttachment(InetProtocol protocol, @Nonnegative int reqBufferSize, @Nonnegative int respBufferSize) {
        this.protocol = protocol;
        this.reqBuffer = ByteBuffer.allocate(reqBufferSize);
        this.respBuffer = ByteBuffer.allocate(respBufferSize);
    }

    @Override
    public InetProtocol getProtocol() {
        return this.protocol;
    }

    @Override
    public void setProtocol(InetProtocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public ByteBuffer getRequestBuffer() {
        return this.reqBuffer;
    }

    @Override
    public void setRequestBuffer(ByteBuffer reqBuffer) {
        this.reqBuffer = reqBuffer;
    }

    @Override
    public ByteBuffer getResponseBuffer() {
        return this.respBuffer;
    }

    @Override
    public void setResponseBuffer(ByteBuffer respBuffer) {
        this.respBuffer = respBuffer;
    }

    @Nullable
    @Override
    public SocketAddress getSocketAddress() {
        return this.socketAddr;
    }

    @Nullable
    @Override
    public SocketAddress setSocketAddress(@Nullable SocketAddress socketAddr) {
        return (this.socketAddr = socketAddr);
    }
}
