package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolLifecycleBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.ClientSocketAdapter;
import gov.hhs.onc.dcdt.net.sockets.SocketAdapter;
import gov.hhs.onc.dcdt.net.sockets.SocketListener;
import gov.hhs.onc.dcdt.net.sockets.SocketRequest;
import gov.hhs.onc.dcdt.net.sockets.SocketRequestProcessor;
import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;
import org.apache.mina.util.ConcurrentHashSet;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;

public abstract class AbstractSocketListener<T extends Closeable, U extends SocketAdapter<T>, V extends Closeable, W extends ClientSocketAdapter<V>, X extends SocketRequest, Y extends SocketRequestProcessor<X>>
    extends AbstractToolLifecycleBean implements SocketListener<T, U, V, W, X, Y> {
    protected class SocketListenDaemon implements Callable<Void> {
        @Nullable
        @Override
        public Void call() throws Exception {
            while ((AbstractSocketListener.this.listenSocketAdapter != null) && !AbstractSocketListener.this.listenSocketAdapter.isClosed()) {
                X req = AbstractSocketListener.this.createRequest(AbstractSocketListener.this.listenSocketAdapter.getProtocol());

                final W reqSocketAdapter = AbstractSocketListener.this.readRequest(AbstractSocketListener.this.listenSocketAdapter, req);
                AbstractSocketListener.this.reqSocketAdapters.add(reqSocketAdapter);

                final ListenableFutureTask<Void> reqDaemonTask = AbstractSocketListener.this.createRequestDaemonTask(reqSocketAdapter, req);
                AbstractSocketListener.this.reqDaemonTasks.add(reqDaemonTask);

                reqDaemonTask.addCallback(new ListenableFutureCallback<Void>() {
                    @Override
                    public void onSuccess(@Nullable Void result) {
                        this.processResponse();
                    }

                    @Override
                    public void onFailure(Throwable th) {
                        this.processResponse();
                    }

                    private void processResponse() {
                        AbstractSocketListener.this.reqDaemonTasks.remove(reqDaemonTask);
                        AbstractSocketListener.this.reqSocketAdapters.remove(reqSocketAdapter);
                    }
                });

                AbstractSocketListener.this.taskExec.execute(reqDaemonTask);
            }

            return null;
        }
    }

    protected class SocketRequestDaemon implements Callable<Void> {
        protected W reqSocketAdapter;
        protected X req;

        protected SocketRequestDaemon(W reqSocketAdapter, X req) {
            this.reqSocketAdapter = reqSocketAdapter;
            this.req = req;
        }

        @Nullable
        @Override
        public Void call() throws Exception {
            AbstractSocketListener.this.writeResponse(this.reqSocketAdapter, AbstractSocketListener.this.createRequestProcessor(req).processRequest(),
                req.getRemoteAddress());

            return null;
        }
    }

    protected Class<T> listenSocketClass;
    protected Class<U> listenSocketAdapterClass;
    protected Class<V> reqSocketClass;
    protected Class<W> reqSocketAdapterClass;
    protected Class<X> reqClass;
    protected Class<Y> reqProcClass;
    protected SocketAddress bindSocketAddr;
    protected AbstractApplicationContext appContext;
    protected U listenSocketAdapter;
    protected Set<W> reqSocketAdapters = new ConcurrentHashSet<>();
    protected ListenableFutureTask<Void> listenDaemonTask;
    protected Set<ListenableFutureTask<Void>> reqDaemonTasks = new ConcurrentHashSet<>();

    protected AbstractSocketListener(Class<T> listenSocketClass, Class<U> listenSocketAdapterClass, Class<V> reqSocketClass, Class<W> reqSocketAdapterClass,
        Class<X> reqClass, Class<Y> reqProcClass, SocketAddress bindSocketAddr) {
        this.listenSocketClass = listenSocketClass;
        this.listenSocketAdapterClass = listenSocketAdapterClass;
        this.reqSocketClass = reqSocketClass;
        this.reqSocketAdapterClass = reqSocketAdapterClass;
        this.reqClass = reqClass;
        this.reqProcClass = reqProcClass;
        this.bindSocketAddr = bindSocketAddr;
    }

    @Override
    public boolean isRunning() {
        return (super.isRunning() && (((this.listenSocketAdapter != null) && !listenSocketAdapter.isClosed()) || !this.reqSocketAdapters.isEmpty()
            || ((this.listenDaemonTask != null) && !this.listenDaemonTask.isDone()) || !this.reqDaemonTasks.isEmpty()));
    }

    @Override
    protected void stopInternal() throws Exception {
        if ((this.listenDaemonTask != null) && !this.listenDaemonTask.isDone()) {
            this.listenDaemonTask.cancel(true);
        }

        if ((this.listenSocketAdapter != null) && !this.listenSocketAdapter.isClosed()) {
            this.listenSocketAdapter.close();
        }

        for (FutureTask<Void> reqDaemonTask : this.reqDaemonTasks) {
            if (!reqDaemonTask.isDone()) {
                reqDaemonTask.cancel(true);
            }
        }

        for (W reqSocketAdapter : this.reqSocketAdapters) {
            if (!reqSocketAdapter.isClosed()) {
                reqSocketAdapter.close();
            }
        }
    }

    @Override
    protected void startInternal() throws Exception {
        (this.listenSocketAdapter = this.createListenSocketAdapter(this.createListenSocket())).bind(this.bindSocketAddr);

        this.taskExec.execute((this.listenDaemonTask = this.createListenDaemonTask()));
    }

    protected void writeResponse(W reqSocketAdapter, byte[] respData, SocketAddress remoteAddr) throws IOException {
        reqSocketAdapter.write(respData, remoteAddr);
    }

    protected Y createRequestProcessor(Object ... reqProcArgs) {
        return ToolBeanFactoryUtils.createBeanOfType(this.appContext, this.reqProcClass, reqProcArgs);
    }

    protected abstract W readRequest(U listenSocketAdapter, X req) throws IOException;

    protected X createRequest(InetProtocol protocol) {
        return ToolBeanFactoryUtils.createBeanOfType(this.appContext, this.reqClass, protocol);
    }

    protected ListenableFutureTask<Void> createRequestDaemonTask(W respSocketAdapter, X req) {
        return new ListenableFutureTask<>(new SocketRequestDaemon(respSocketAdapter, req));
    }

    protected ListenableFutureTask<Void> createListenDaemonTask() {
        return new ListenableFutureTask<>(new SocketListenDaemon());
    }

    protected W createRequestSocketAdapter(V reqSocket) throws IOException {
        return ToolBeanFactoryUtils.createBeanOfType(this.appContext, this.reqSocketAdapterClass, reqSocket);
    }

    protected U createListenSocketAdapter(T listenSocket) throws IOException {
        return ToolBeanFactoryUtils.createBeanOfType(this.appContext, this.listenSocketAdapterClass, listenSocket);
    }

    protected T createListenSocket(Object ... listenSocketArgs) {
        return ToolBeanFactoryUtils.createBeanOfType(this.appContext, this.listenSocketClass, listenSocketArgs);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }
}
