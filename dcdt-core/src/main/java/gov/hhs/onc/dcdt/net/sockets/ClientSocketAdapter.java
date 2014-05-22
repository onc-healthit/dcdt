package gov.hhs.onc.dcdt.net.sockets;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import javax.annotation.Nonnegative;
import org.apache.commons.lang3.tuple.Pair;

public interface ClientSocketAdapter<T extends Closeable> extends SocketAdapter<T> {
    public void write(byte[] data, SocketAddress remoteAddr) throws IOException;

    public Pair<SocketAddress, byte[]> read() throws IOException;

    public Pair<SocketAddress, byte[]> read(@Nonnegative int bufferLen) throws IOException;

    public Pair<SocketAddress, byte[]> read(byte[] buffer) throws IOException;

    @Nonnegative
    public int getSendBufferSize() throws IOException;

    public void setSendBufferSize(@Nonnegative int sendBufferSize) throws IOException;
}
