package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;
import org.xbill.DNS.DClass;

public enum DnsDclassType implements DnsMnemonicIdentifier {
    IN(DClass.IN), CH(DClass.CH), CHAOS(DClass.CHAOS), HS(DClass.HS), HESIOD(DClass.HESIOD), NONE(DClass.NONE), ANY(DClass.ANY);

    private final int code;
    private final String id;

    private DnsDclassType(@Nonnegative int code) {
        this.code = code;
        this.id = DClass.string(this.code);
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
