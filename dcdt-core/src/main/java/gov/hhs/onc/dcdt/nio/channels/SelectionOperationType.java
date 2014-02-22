package gov.hhs.onc.dcdt.nio.channels;

import java.nio.channels.SelectionKey;

public enum SelectionOperationType {
    READ(SelectionKey.OP_READ), WRITE(SelectionKey.OP_WRITE), ACCEPT(SelectionKey.OP_ACCEPT), CONNECT(SelectionKey.OP_CONNECT);

    private final int op;

    private SelectionOperationType(int op) {
        this.op = op;
    }

    public boolean isReady(SelectionKey selKey) {
        return this.isReady(selKey.readyOps());
    }

    public boolean isReady(int readyOps) {
        return (readyOps & this.op) != 0;
    }

    public int getOperation() {
        return this.op;
    }
}
