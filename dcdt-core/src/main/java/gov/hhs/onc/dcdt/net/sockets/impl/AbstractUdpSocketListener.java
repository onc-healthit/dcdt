package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.net.sockets.SocketRequest;
import gov.hhs.onc.dcdt.net.sockets.SocketRequestProcessor;
import gov.hhs.onc.dcdt.net.sockets.UdpSocketAdapter;
import gov.hhs.onc.dcdt.net.sockets.UdpSocketListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractUdpSocketListener<T extends UdpSocketAdapter, U extends SocketRequest, V extends SocketRequestProcessor<U>> extends
    AbstractSocketListener<DatagramSocket, T, DatagramSocket, T, U, V> implements UdpSocketListener<T, U, V> {
    protected AbstractUdpSocketListener(Class<T> socketAdapterClass, Class<U> reqClass, Class<V> reqProcClass, SocketAddress bindSocketAddr) {
        super(DatagramSocket.class, socketAdapterClass, DatagramSocket.class, socketAdapterClass, reqClass, reqProcClass, bindSocketAddr);
    }

    @Override
    protected T readRequest(T listenSocketAdapter, U req) throws IOException {
        ByteBuffer reqBuffer = req.getRequestBuffer();
        Pair<SocketAddress, byte[]> reqPair = listenSocketAdapter.read(reqBuffer.remaining());

        req.setRemoteAddress(reqPair.getLeft());

        reqBuffer.put(reqPair.getRight());

        return listenSocketAdapter;
    }

    @Override
    protected DatagramSocket createListenSocket(Object ... listenSocketArgs) {
        return super.createListenSocket(((SocketAddress) null));
    }
}
