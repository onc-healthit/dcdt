package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolLifecycleBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.concurrent.ToolListenableFutureCallback;
import gov.hhs.onc.dcdt.concurrent.ToolListenableFutureTask;
import gov.hhs.onc.dcdt.concurrent.impl.AbstractCancelRejectedFutureHandler;
import gov.hhs.onc.dcdt.concurrent.impl.AbstractToolListenableFutureCallback;
import gov.hhs.onc.dcdt.concurrent.impl.AbstractToolListenableFutureTask;
import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.ClientSocketAdapter;
import gov.hhs.onc.dcdt.net.sockets.SocketAdapter;
import gov.hhs.onc.dcdt.net.sockets.SocketListener;
import gov.hhs.onc.dcdt.net.sockets.SocketRequest;
import gov.hhs.onc.dcdt.net.sockets.SocketRequestProcessor;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Nullable;
import org.apache.mina.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFutureTask;

public abstract class AbstractSocketListener<T extends Closeable, U extends SocketAdapter<T>, V extends Closeable, W extends ClientSocketAdapter<V>, X extends SocketRequest, Y extends SocketRequestProcessor<X>>
    extends AbstractToolLifecycleBean implements SocketListener<T, U, V, W, X, Y> {
    protected class SocketListenDaemon implements Callable<Void> {
        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public Void call() throws Exception {
            AbstractSocketListener.this.reqTaskExec.setRejectedExecutionHandler(new CancelSocketRequestDaemonHandler<>(SocketRequestDaemonTask.class));

            while ((AbstractSocketListener.this.listenSocketAdapter != null) && !AbstractSocketListener.this.listenSocketAdapter.isClosed()) {
                X req = AbstractSocketListener.this.createRequest(AbstractSocketListener.this.listenSocketAdapter.getProtocol());

                W reqSocketAdapter = AbstractSocketListener.this.readRequest(AbstractSocketListener.this.listenSocketAdapter, req);
                AbstractSocketListener.this.reqSocketAdapters.add(reqSocketAdapter);

                ToolListenableFutureTask<Void> reqDaemonTask = AbstractSocketListener.this.createRequestDaemonTask(reqSocketAdapter, req);
                reqDaemonTask.getCallbacks().addAll(AbstractSocketListener.this.createRequestDaemonCallbacks(reqSocketAdapter, reqDaemonTask));
                AbstractSocketListener.this.reqDaemonTasks.add(reqDaemonTask);

                AbstractSocketListener.this.reqTaskExec.execute(reqDaemonTask);
            }

            return null;
        }
    }

    protected class CancelSocketRequestDaemonHandler<Z extends SocketRequestDaemonTask> extends AbstractCancelRejectedFutureHandler<Void, Z> {
        public CancelSocketRequestDaemonHandler(Class<Z> taskClass) {
            super(Void.class, taskClass);
        }

        @Override
        protected void rejectedExecutionInternal(Z task, ThreadPoolExecutor executor) {
            super.rejectedExecutionInternal(task, executor);

            W reqSocketAdapter = task.getRequestDaemon().getRequestSocketAdapter();

            AbstractSocketListener.LOGGER.warn(String.format(
                "Cancelled socket (class=%s, adapterClass=%s, protocol=%s, localAddr={%s}, remoteAddr={%s}) for request daemon task (class=%s).",
                ToolClassUtils.getName(reqSocketAdapter.getSocket()), ToolClassUtils.getName(reqSocketAdapter), reqSocketAdapter.getProtocol().name(),
                reqSocketAdapter.getLocalSocketAddress(), reqSocketAdapter.getRemoteSocketAddress(), ToolClassUtils.getName(task)));
        }
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    protected class SocketRequestDaemonCleanupCallback extends AbstractSocketRequestDaemonCallback {
        public SocketRequestDaemonCleanupCallback(W reqSocketAdapter, ToolListenableFutureTask<Void> reqDaemonTask) {
            super(reqSocketAdapter, reqDaemonTask);
        }

        @Override
        protected void onPostDone(@Nullable Void result, @Nullable Throwable th) {
            AbstractSocketListener.this.reqDaemonTasks.remove(this.task);
            AbstractSocketListener.this.reqSocketAdapters.remove(this.reqSocketAdapter);
        }
    }

    protected abstract class AbstractSocketRequestDaemonCallback extends AbstractToolListenableFutureCallback<Void, ToolListenableFutureTask<Void>> {
        protected W reqSocketAdapter;

        protected AbstractSocketRequestDaemonCallback(W reqSocketAdapter, ToolListenableFutureTask<Void> reqDaemonTask) {
            super(reqDaemonTask);

            this.reqSocketAdapter = reqSocketAdapter;
        }
    }

    protected class SocketRequestDaemonTask extends AbstractToolListenableFutureTask<Void> {
        private SocketRequestDaemon reqDaemon;

        public SocketRequestDaemonTask(SocketRequestDaemon reqDaemon) {
            super(reqDaemon);

            this.reqDaemon = reqDaemon;
        }

        public SocketRequestDaemon getRequestDaemon() {
            return this.reqDaemon;
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

        public X getRequest() {
            return this.req;
        }

        public W getRequestSocketAdapter() {
            return this.reqSocketAdapter;
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
    protected ThreadPoolTaskExecutor reqTaskExec;
    protected Set<ToolListenableFutureTask<Void>> reqDaemonTasks = new ConcurrentHashSet<>();

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractSocketListener.class);

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

        for (ToolListenableFutureTask<Void> reqDaemonTask : this.reqDaemonTasks) {
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

    protected List<ToolListenableFutureCallback<Void, ToolListenableFutureTask<Void>>> createRequestDaemonCallbacks(W reqSocketAdapter,
        ToolListenableFutureTask<Void> reqDaemonTask) {
        return ToolArrayUtils.asList((ToolListenableFutureCallback<Void, ToolListenableFutureTask<Void>>) new SocketRequestDaemonCleanupCallback(
            reqSocketAdapter, reqDaemonTask));
    }

    protected SocketRequestDaemonTask createRequestDaemonTask(W reqSocketAdapter, X req) {
        return new SocketRequestDaemonTask(new SocketRequestDaemon(reqSocketAdapter, req));
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

    protected void setRequestTaskExecutor(ThreadPoolTaskExecutor reqTaskExec) {
        this.reqTaskExec = reqTaskExec;
    }
}
