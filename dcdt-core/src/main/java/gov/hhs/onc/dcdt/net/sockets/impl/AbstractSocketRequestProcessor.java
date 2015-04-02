package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.net.sockets.SocketRequest;
import gov.hhs.onc.dcdt.net.sockets.SocketRequestProcessor;
import gov.hhs.onc.dcdt.nio.utils.ToolBufferUtils;
import java.nio.ByteBuffer;

public abstract class AbstractSocketRequestProcessor<T extends SocketRequest> extends AbstractToolBean implements SocketRequestProcessor<T> {
    protected T req;

    protected AbstractSocketRequestProcessor(T req) {
        this.req = req;
    }

    @Override
    public byte[] processRequest() {
        byte[] reqData = ToolBufferUtils.get(ToolBufferUtils.flip(this.req.getRequestBuffer()));
        ByteBuffer respBuffer = this.req.getResponseBuffer();

        try {
            respBuffer.put(this.processRequestInternal(reqData));
        } catch (Exception e) {
            respBuffer.put(this.processError(reqData, e));
        }

        return ToolBufferUtils.get(ToolBufferUtils.flip(respBuffer));
    }

    protected abstract byte[] processError(byte[] reqData, Exception exception);

    protected abstract byte[] processRequestInternal(byte[] reqData) throws Exception;
}
