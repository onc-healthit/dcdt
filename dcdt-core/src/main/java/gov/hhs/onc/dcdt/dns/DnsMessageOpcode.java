package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;
import org.xbill.DNS.Opcode;

public enum DnsMessageOpcode implements DnsMnemonicIdentifier {
    QUERY(Opcode.QUERY), IQUERY(Opcode.IQUERY), STATUS(Opcode.STATUS), NOTIFY(Opcode.NOTIFY), UPDATE(Opcode.UPDATE);

    private final int code;
    private final String id;

    private DnsMessageOpcode(@Nonnegative int code) {
        this.code = code;
        this.id = Opcode.string(code);
    }

    @Nonnegative
    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
