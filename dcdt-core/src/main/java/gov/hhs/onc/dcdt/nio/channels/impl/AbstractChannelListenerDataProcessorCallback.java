package gov.hhs.onc.dcdt.nio.channels.impl;

import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.nio.ToolNioRuntimeException;
import gov.hhs.onc.dcdt.nio.channels.ChannelListenerDataProcessor;
import gov.hhs.onc.dcdt.nio.channels.ChannelListenerDataProcessorCallback;
import gov.hhs.onc.dcdt.nio.channels.SelectionAttachment;
import gov.hhs.onc.dcdt.nio.channels.SelectionOperationType;
import gov.hhs.onc.dcdt.nio.utils.ToolBufferUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.nio.channels.SelectionKey;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractChannelListenerDataProcessorCallback<T extends ChannelListenerDataProcessor, U extends SelectionAttachment> implements
    ChannelListenerDataProcessorCallback<T, U> {
    protected Class<T> dataProcClass;
    protected SelectionOperationType readOpType;
    protected SelectionOperationType writeOpType;
    protected InetProtocol protocol;
    protected SelectionKey selKey;
    protected U selAttachment;

    public AbstractChannelListenerDataProcessorCallback(Class<T> dataProcClass, SelectionOperationType readOpType, SelectionOperationType writeOpType,
        InetProtocol protocol, SelectionKey selKey, U selAttachment) {
        this.dataProcClass = dataProcClass;
        this.readOpType = readOpType;
        this.writeOpType = writeOpType;
        this.protocol = protocol;
        this.selKey = selKey;
        this.selAttachment = selAttachment;
    }

    @Override
    public void onSuccess(byte[] respData) {
        this.setResponseData(respData);
    }

    @Override
    public void onFailure(Throwable th) {
        throw new ToolNioRuntimeException(String.format("Unable to process (class=%s) channel listener data.", ToolClassUtils.getName(this.dataProcClass)), th);
    }

    protected void setResponseData(byte[] respData) {
        ToolBufferUtils.clear(this.selAttachment.getResponseBuffer()).put(respData);

        this.selKey.interestOps(this.writeOpType.getOperation());

        this.selKey.selector().wakeup();
    }
}
