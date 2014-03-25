package gov.hhs.onc.dcdt.nio.channels.impl;

import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.nio.channels.ChannelListenerDataProcessor;
import gov.hhs.onc.dcdt.nio.channels.ChannelListenerDataProcessorCallback;
import gov.hhs.onc.dcdt.nio.channels.SelectionAttachment;
import gov.hhs.onc.dcdt.nio.channels.SelectionOperationType;
import gov.hhs.onc.dcdt.nio.utils.ToolBufferUtils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractUdpChannelListener<T extends SelectionAttachment, U extends ChannelListenerDataProcessor, V extends ChannelListenerDataProcessorCallback<U, T>>
    extends AbstractChannelListener<DatagramChannel, DatagramChannel, DatagramChannel, T, U, V> {
    protected AbstractUdpChannelListener(Class<T> attachmentClass, Class<U> dataProcClass, Class<V> dataProcCallbackClass, InetSocketAddress bindSocketAddr) {
        super(DatagramChannel.class, DatagramChannel.class, DatagramChannel.class, attachmentClass, dataProcClass, dataProcCallbackClass,
            SelectionOperationType.READ, SelectionOperationType.READ, SelectionOperationType.WRITE, InetProtocol.UDP, bindSocketAddr);
    }

    @Override
    protected boolean processWriteOperation(SelectionKey selKey, DatagramChannel writeChannel, T selAttachment) throws IOException {
        writeChannel.send(ToolBufferUtils.flip(selAttachment.getResponseBuffer()), selAttachment.getSocketAddress());

        return super.processWriteOperation(selKey, writeChannel, selAttachment);
    }

    @Override
    protected boolean processReadOperation(SelectionKey selKey, DatagramChannel readChannel, T selAttachment) throws IOException {
        return (selAttachment.setSocketAddress(readChannel.receive(selAttachment.getRequestBuffer())) != null)
            && (selAttachment.getRequestBuffer().position() > 0) && super.processReadOperation(selKey, readChannel, selAttachment);
    }
}
