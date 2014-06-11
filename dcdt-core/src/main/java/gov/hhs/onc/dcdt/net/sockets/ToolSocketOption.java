package gov.hhs.onc.dcdt.net.sockets;

import java.net.SocketOption;
import javax.annotation.Nullable;

public class ToolSocketOption<T> implements SocketOption<T> {
    private String name;
    private Class<T> type;

    public ToolSocketOption(Class<T> type) {
        this(null, type);
    }

    public ToolSocketOption(@Nullable String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    @Nullable
    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Class<T> type() {
        return this.type;
    }
}
