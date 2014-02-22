package gov.hhs.onc.dcdt.nio.channels.impl;

import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.nio.channels.ChannelListenerDataProcessor;
import gov.hhs.onc.dcdt.nio.channels.ChannelListenerDataProcessorCallback;
import gov.hhs.onc.dcdt.nio.channels.SelectionAttachment;
import gov.hhs.onc.dcdt.nio.channels.SelectionOperationType;
import gov.hhs.onc.dcdt.nio.utils.ToolBufferUtils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractTcpChannelListener<T extends SelectionAttachment, U extends ChannelListenerDataProcessor, V extends ChannelListenerDataProcessorCallback<U, T>>
    extends AbstractChannelListener<ServerSocketChannel, SocketChannel, SocketChannel, T, U, V> {
    protected AbstractTcpChannelListener(Class<T> attachmentClass, Class<U> dataProcClass, Class<V> dataProcCallbackClass, InetSocketAddress bindSocketAddr) {
        super(ServerSocketChannel.class, SocketChannel.class, SocketChannel.class, attachmentClass, dataProcClass, dataProcCallbackClass,
            SelectionOperationType.ACCEPT, SelectionOperationType.READ, SelectionOperationType.WRITE, InetProtocol.TCP, bindSocketAddr);
    }

    @Override
    protected boolean processWriteOperation(SelectionKey selKey, SocketChannel writeChannel, T selAttachment) throws IOException {
        ByteBuffer respBuffer = selAttachment.getResponseBuffer();

        if (respBuffer.position() > 0) {
            writeChannel.write(ToolBufferUtils.flip(respBuffer));

            if (respBuffer.hasRemaining()) {
                respBuffer.compact();

                selKey.selector().wakeup();

                return false;
            }
        }

        return super.processWriteOperation(selKey, writeChannel, selAttachment);
    }

    @Override
    protected boolean processReadOperation(SelectionKey selKey, SocketChannel readChannel, T selAttachment) throws IOException {
        ByteBuffer reqBuffer = selAttachment.getRequestBuffer();

        if (reqBuffer.limit() == 0) {
            reqBuffer.limit(2);
        }

        if (readChannel.read(reqBuffer) == -1) {
            readChannel.close();
            selKey.cancel();

            return false;
        }

        if (!reqBuffer.hasRemaining()) {
            if (reqBuffer.limit() != 2) {
                return super.processReadOperation(selKey, readChannel, selAttachment);
            } else {
                reqBuffer.limit((((reqBuffer.get(0) & 0xFF) << 8) + (reqBuffer.get(1) & 0xFF)));
                reqBuffer.position(0);
            }
        }

        selKey.selector().wakeup();

        return false;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    protected boolean processListenOperation(SelectionKey selKey, ServerSocketChannel listenChannel, T selAttachment) throws IOException {
        SocketChannel readChannel = listenChannel.accept();

        if (readChannel != null) {
            this.initializeChannel(readChannel).register(selKey.selector(), this.readOpType.getOperation(), this.createAttachment());

            return super.processListenOperation(selKey, listenChannel, selAttachment);
        } else {
            return false;
        }
    }

    @Override
    protected T createAttachment() {
        T attachment = super.createAttachment();
        attachment.getRequestBuffer().limit(0);

        return attachment;
    }
}
