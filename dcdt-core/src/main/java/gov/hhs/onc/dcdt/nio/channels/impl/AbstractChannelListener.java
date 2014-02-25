package gov.hhs.onc.dcdt.nio.channels.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.nio.channels.ChannelListener;
import gov.hhs.onc.dcdt.nio.channels.ChannelListenerDataProcessor;
import gov.hhs.onc.dcdt.nio.channels.ChannelListenerDataProcessorCallback;
import gov.hhs.onc.dcdt.nio.channels.ChannelSocketOptions;
import gov.hhs.onc.dcdt.nio.channels.SelectionAttachment;
import gov.hhs.onc.dcdt.nio.channels.SelectionOperationType;
import gov.hhs.onc.dcdt.nio.utils.ToolBufferUtils;
import gov.hhs.onc.dcdt.nio.utils.ToolChannelUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolMapUtils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractChannelListener<T extends SelectableChannel & NetworkChannel, U extends SelectableChannel & ByteChannel & NetworkChannel, V extends SelectableChannel & ByteChannel & NetworkChannel, W extends SelectionAttachment, X extends ChannelListenerDataProcessor, Y extends ChannelListenerDataProcessorCallback<X, W>>
    extends AbstractToolBean implements ChannelListener<T, U, V, W, X, Y> {
    @SuppressWarnings({ "unchecked" })
    protected final static List<? extends Pair<? extends SocketOption<?>, ?>> OPT_PAIRS_CHANNEL = ToolArrayUtils.asList(new MutablePair<>(
        StandardSocketOptions.SO_REUSEADDR, true), new MutablePair<>(ChannelSocketOptions.CHANNEL_BLOCKING, false));

    protected AbstractApplicationContext appContext;
    protected AsyncListenableTaskExecutor taskExec;
    protected Class<T> listenChannelClass;
    protected Class<U> readChannelClass;
    protected Class<V> writeChannelClass;
    protected Class<W> attachmentClass;
    protected Class<X> dataProcClass;
    protected Class<Y> dataProcCallbackClass;
    protected SelectionOperationType listenOpType;
    protected SelectionOperationType readOpType;
    protected SelectionOperationType writeOpType;
    protected InetProtocol protocol;
    protected InetSocketAddress bindSocketAddr;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractChannelListener.class);

    protected AbstractChannelListener(Class<T> listenChannelClass, Class<U> readChannelClass, Class<V> writeChannelClass, Class<W> attachmentClass,
        Class<X> dataProcClass, Class<Y> dataProcCallbackClass, SelectionOperationType listenOpType, SelectionOperationType readOpType,
        SelectionOperationType writeOpType, InetProtocol protocol, InetSocketAddress bindSocketAddr) {
        this.listenChannelClass = listenChannelClass;
        this.readChannelClass = readChannelClass;
        this.writeChannelClass = writeChannelClass;
        this.attachmentClass = attachmentClass;
        this.dataProcClass = dataProcClass;
        this.dataProcCallbackClass = dataProcCallbackClass;
        this.listenOpType = listenOpType;
        this.readOpType = readOpType;
        this.writeOpType = writeOpType;
        this.protocol = protocol;
        this.bindSocketAddr = bindSocketAddr;
    }

    @Nullable
    @Override
    public SelectionOperationType processSelection(SelectionKey selKey) throws IOException {
        Pair<SelectionOperationType, ? extends Class<? extends SelectableChannel>> selOp = this.getSelectedOperation(selKey);

        if (selOp != null) {
            SelectionOperationType selOpType = selOp.getLeft();

            W selAttachment = this.attachmentClass.cast(selKey.attachment());
            boolean selProcessed;

            if (selOpType == this.readOpType) {
                selProcessed = this.processReadOperation(selKey, this.readChannelClass.cast(selKey.channel()), selAttachment);
            } else if (selOpType == this.writeOpType) {
                selProcessed = this.processWriteOperation(selKey, this.writeChannelClass.cast(selKey.channel()), selAttachment);
            } else {
                selProcessed = this.processListenOperation(selKey, this.listenChannelClass.cast(selKey.channel()), selAttachment);
            }

            if (selProcessed) {
                LOGGER.trace(String.format("Channel listener (class=%s) processed selection (opType=%s, channelClass=%s, attachmentClass=%s).",
                    ToolClassUtils.getName(this), selOpType.name(), ToolClassUtils.getName(selOp.getRight()), ToolClassUtils.getName(selAttachment)));
            }

            return selOpType;
        } else {
            return null;
        }
    }

    @Override
    public void register(Selector selector) throws IOException {
        try {
            this.createListenChannel().register(selector, this.listenOpType.getOperation(), this.createAttachment());
        } catch (ClosedChannelException ignored) {
        }
    }

    protected static boolean isOperationSelected(SelectionKey selKey, SelectionOperationType selOpType, Class<? extends SelectableChannel> selOpChannelClass) {
        return selOpType.isReady(selKey) && ToolClassUtils.isAssignable(selKey.channel().getClass(), selOpChannelClass);
    }

    protected boolean processWriteOperation(SelectionKey selKey, V writeChannel, W selAttachment) throws IOException {
        selKey.attach(this.createAttachment());
        selKey.interestOps(this.readOpType.getOperation());
        selKey.selector().wakeup();

        return true;
    }

    protected boolean processReadOperation(SelectionKey selKey, U readChannel, W selAttachment) throws IOException {
        this.executeDataProcessorTask(ToolBufferUtils.flip(selAttachment.getRequestBuffer()), this.createDataProcessorCallbacks(selKey, selAttachment));

        return true;
    }

    protected boolean processListenOperation(SelectionKey selKey, T listenChannel, W selAttachment) throws IOException {
        selKey.selector().wakeup();

        return true;
    }

    protected void executeDataProcessorTask(ByteBuffer reqBuffer, Iterable<? extends ListenableFutureCallback<byte[]>> dataProcCallbacks) {
        ListenableFutureTask<byte[]> dataProcFutureTask = new ListenableFutureTask<>(this.createDataProcessor(this.protocol, ToolBufferUtils.get(reqBuffer)));

        for (ListenableFutureCallback<byte[]> dataProcCallback : dataProcCallbacks) {
            dataProcFutureTask.addCallback(dataProcCallback);
        }

        this.taskExec.execute(dataProcFutureTask);
    }

    @Nullable
    protected Pair<SelectionOperationType, ? extends Class<? extends SelectableChannel>> getSelectedOperation(SelectionKey selKey) {
        SelectionOperationType selOpType;
        Class<? extends SelectableChannel> selChannelClass;

        return (isOperationSelected(selKey, (selOpType = this.readOpType), (selChannelClass = this.readChannelClass))
            || isOperationSelected(selKey, (selOpType = this.writeOpType), (selChannelClass = this.writeChannelClass)) || isOperationSelected(selKey,
                (selOpType = this.listenOpType), (selChannelClass = this.listenChannelClass))) ? new MutablePair<>(selOpType, selChannelClass) : null;
    }

    protected List<? extends Y> createDataProcessorCallbacks(SelectionKey selKey, W selAttachment) {
        return ToolBeanFactoryUtils.createBeansOfType(this.appContext, this.dataProcCallbackClass, this.readOpType, this.writeOpType, this.protocol, selKey,
            selAttachment);
    }

    protected X createDataProcessor(Object ... beanCreationArgs) {
        return ToolBeanFactoryUtils.createBeanOfType(this.appContext, this.dataProcClass, beanCreationArgs);
    }

    protected W createAttachment() {
        return ToolBeanFactoryUtils.createBeanOfType(this.appContext, this.attachmentClass, this.protocol);
    }

    protected T createListenChannel() throws IOException {
        return this.listenChannelClass.cast(this.initializeChannel(ToolBeanFactoryUtils.createBeanOfType(this.appContext, this.listenChannelClass)).bind(
            this.bindSocketAddr));
    }

    protected <Z extends SelectableChannel & NetworkChannel> Z initializeChannel(Z channel) throws IOException {
        Map<SocketOption<?>, Object> optMap = ToolMapUtils.putAll(new LinkedMap<SocketOption<?>, Object>(), OPT_PAIRS_CHANNEL);
        Class<? extends SelectableChannel> channelClass = channel.getClass();
        int bufferSize = this.protocol.getDataSizeMax();

        if (ToolClassUtils.isAssignable(channelClass, this.readChannelClass)) {
            // noinspection ConstantConditions
            optMap.put(StandardSocketOptions.SO_RCVBUF, bufferSize);
        }

        if (ToolClassUtils.isAssignable(channelClass, this.writeChannelClass)) {
            // noinspection ConstantConditions
            optMap.put(StandardSocketOptions.SO_SNDBUF, bufferSize);
        }

        return ToolChannelUtils.setOptions(channel, optMap);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        this.taskExec = taskExec;
    }
}
