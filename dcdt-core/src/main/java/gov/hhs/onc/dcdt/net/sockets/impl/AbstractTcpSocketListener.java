package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.concurrent.ToolListenableFutureCallback;
import gov.hhs.onc.dcdt.concurrent.ToolListenableFutureTask;
import gov.hhs.onc.dcdt.net.sockets.SocketRequest;
import gov.hhs.onc.dcdt.net.sockets.SocketRequestProcessor;
import gov.hhs.onc.dcdt.net.sockets.TcpServerSocketAdapter;
import gov.hhs.onc.dcdt.net.sockets.TcpSocketAdapter;
import gov.hhs.onc.dcdt.net.sockets.TcpSocketListener;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

public class AbstractTcpSocketListener<T extends TcpServerSocketAdapter, U extends TcpSocketAdapter, V extends SocketRequest, W extends SocketRequestProcessor<V>>
    extends AbstractSocketListener<ServerSocket, T, Socket, U, V, W> implements TcpSocketListener<T, U, V, W> {
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    protected class SocketRequestDaemonTcpCleanupCallback extends AbstractSocketRequestDaemonCallback {
        public SocketRequestDaemonTcpCleanupCallback(U reqSocketAdapter, ToolListenableFutureTask<Void> reqDaemonTask) {
            super(reqSocketAdapter, reqDaemonTask);
        }

        @Override
        protected void onPostDone(@Nullable Void result, @Nullable Throwable th) {
            try {
                reqSocketAdapter.close();
            } catch (IOException e) {
                AbstractTcpSocketListener.LOGGER
                    .warn(
                        String
                            .format(
                                "Unable to close socket (class=%s, adapterClass=%s, protocol=%s, localAddr={%s}, remoteAddr={%s}) after completion of request daemon task (class=%s, status=%s).",
                                ToolClassUtils.getName(reqSocketAdapter.getSocket()), ToolClassUtils.getName(reqSocketAdapter), reqSocketAdapter.getProtocol()
                                    .name(), reqSocketAdapter.getLocalSocketAddress(), reqSocketAdapter.getRemoteSocketAddress(), ToolClassUtils
                                    .getName(this.task), status), e);
            }
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractTcpSocketListener.class);

    protected AbstractTcpSocketListener(Class<T> listenSocketAdapterClass, Class<U> reqSocketAdapterClass, Class<V> reqClass, Class<W> reqProcClass,
        SocketAddress bindSocketAddr) {
        super(ServerSocket.class, listenSocketAdapterClass, Socket.class, reqSocketAdapterClass, reqClass, reqProcClass, bindSocketAddr);
    }

    @Override
    protected U readRequest(T listenSocketAdapter, V req) throws IOException {
        U reqSocketAdapter = this.createRequestSocketAdapter(listenSocketAdapter.accept());
        ByteBuffer reqBuffer = req.getRequestBuffer();
        Pair<SocketAddress, byte[]> reqPair = reqSocketAdapter.read(reqBuffer.remaining());

        req.setRemoteAddress(reqPair.getLeft());

        reqBuffer.put(reqPair.getRight());

        return reqSocketAdapter;
    }

    @Override
    protected List<ToolListenableFutureCallback<Void, ToolListenableFutureTask<Void>>> createRequestDaemonCallbacks(U reqSocketAdapter,
        ToolListenableFutureTask<Void> reqDaemonTask) {
        return ToolCollectionUtils.add(super.createRequestDaemonCallbacks(reqSocketAdapter, reqDaemonTask), new SocketRequestDaemonTcpCleanupCallback(
            reqSocketAdapter, reqDaemonTask));
    }
}
