package gov.hhs.onc.dcdt.net.sockets;

import java.net.SocketOption;

public class ToolSocketOption<T> implements SocketOption<T> {
    private String name;
    private Class<T> type;

    public ToolSocketOption(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Class<T> type() {
        return this.type;
    }
}
