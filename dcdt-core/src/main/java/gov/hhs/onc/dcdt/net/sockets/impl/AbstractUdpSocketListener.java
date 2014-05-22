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

public abstract class AbstractUdpSocketListener<T extends SocketRequest, U extends SocketRequestProcessor<T>> extends
    AbstractSocketListener<DatagramSocket, UdpSocketAdapter, DatagramSocket, UdpSocketAdapter, T, U> implements UdpSocketListener<T, U> {
    protected AbstractUdpSocketListener(Class<T> reqClass, Class<U> reqProcClass, SocketAddress bindSocketAddr) {
        super(DatagramSocket.class, UdpSocketAdapter.class, DatagramSocket.class, UdpSocketAdapter.class, reqClass, reqProcClass, bindSocketAddr);
    }

    @Override
    protected UdpSocketAdapter readRequest(UdpSocketAdapter listenSocketAdapter, T req) throws IOException {
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
