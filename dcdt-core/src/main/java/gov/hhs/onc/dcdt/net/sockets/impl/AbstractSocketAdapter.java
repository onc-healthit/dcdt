package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.net.sockets.SocketAdapter;
import gov.hhs.onc.dcdt.net.sockets.SocketAdapterOptions;
import gov.hhs.onc.dcdt.net.ToolNetException;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.Closeable;
import java.io.IOException;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractSocketAdapter<T extends Closeable> extends AbstractToolBean implements SocketAdapter<T> {
    protected T socket;

    protected AbstractSocketAdapter(T socket) {
        this.socket = socket;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void setOptions(Map<? extends SocketOption<?>, ?> optMap) throws IOException {
        for (Entry<? extends SocketOption<?>, ?> optEntry : optMap.entrySet()) {
            this.setOption(((SocketOption<Object>) optEntry.getKey()), optEntry.getValue());
        }
    }

    @Override
    public <U> void setOption(SocketOption<U> optKey, U optValue) throws IOException {
        try {
            this.setOptionInternal(optKey, optValue);
        } catch (ToolNetException e) {
            throw e;
        } catch (IOException e) {
            throw new ToolNetException(String.format("Unable to set socket (class=%s) adapter (class=%s) option (key=%s) value: %s",
                ToolClassUtils.getName(this.socket), ToolClassUtils.getName(this), optKey, optValue), e);
        }
    }

    protected <U> void setOptionInternal(SocketOption<U> optKey, U optValue) throws IOException {
        if (optKey == StandardSocketOptions.SO_RCVBUF) {
            this.setReceiveBufferSize(((Integer) optValue));
        } else if (optKey == StandardSocketOptions.SO_REUSEADDR) {
            this.setReuseAddress(((Boolean) optValue));
        } else if (optKey == SocketAdapterOptions.TIMEOUT) {
            this.setTimeout(((Integer) optValue));
        } else {
            throw new ToolNetException(String.format("Unable to set unknown socket (class=%s) adapter (class=%s) option (key=%s) value: %s",
                ToolClassUtils.getName(this.socket), ToolClassUtils.getName(this), optKey, optValue));
        }
    }
}
