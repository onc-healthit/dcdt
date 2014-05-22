package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.net.sockets.SocketRequest;
import gov.hhs.onc.dcdt.net.sockets.SocketRequestProcessor;
import gov.hhs.onc.dcdt.net.sockets.TcpServerSocketAdapter;
import gov.hhs.onc.dcdt.net.sockets.TcpSocketAdapter;
import gov.hhs.onc.dcdt.net.sockets.TcpSocketListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import org.apache.commons.lang3.tuple.Pair;

public class AbstractTcpSocketListener<T extends SocketRequest, U extends SocketRequestProcessor<T>> extends
    AbstractSocketListener<ServerSocket, TcpServerSocketAdapter, Socket, TcpSocketAdapter, T, U> implements TcpSocketListener<T, U> {
    protected AbstractTcpSocketListener(Class<T> reqClass, Class<U> reqProcClass, SocketAddress bindSocketAddr) {
        super(ServerSocket.class, TcpServerSocketAdapter.class, Socket.class, TcpSocketAdapter.class, reqClass, reqProcClass, bindSocketAddr);
    }

    @Override
    protected TcpSocketAdapter readRequest(TcpServerSocketAdapter listenSocketAdapter, T req) throws IOException {
        TcpSocketAdapter reqSocketAdapter = this.createRequestSocketAdapter(listenSocketAdapter.accept());
        ByteBuffer reqBuffer = req.getRequestBuffer();
        Pair<SocketAddress, byte[]> reqPair = reqSocketAdapter.read(reqBuffer.remaining());

        req.setRemoteAddress(reqPair.getLeft());

        reqBuffer.put(reqPair.getRight());

        return reqSocketAdapter;
    }
}
