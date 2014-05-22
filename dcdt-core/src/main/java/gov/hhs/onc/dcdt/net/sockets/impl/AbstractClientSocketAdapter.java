package gov.hhs.onc.dcdt.net.sockets.impl;

import gov.hhs.onc.dcdt.net.sockets.ClientSocketAdapter;
import java.io.Closeable;
import java.io.IOException;
import java.net.SocketOption;
import java.net.StandardSocketOptions;

public abstract class AbstractClientSocketAdapter<T extends Closeable> extends AbstractSocketAdapter<T> implements ClientSocketAdapter<T> {
    protected AbstractClientSocketAdapter(T socket) {
        super(socket);
    }

    @Override
    protected <U> void setOptionInternal(SocketOption<U> optKey, U optValue) throws IOException {
        if (optKey == StandardSocketOptions.SO_SNDBUF) {
            this.setSendBufferSize(((Integer) optValue));
        } else {
            super.setOptionInternal(optKey, optValue);
        }
    }
}
