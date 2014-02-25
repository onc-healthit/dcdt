package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.DClass;

public enum DnsDclassType {
    IN(DClass.IN), CH(DClass.CH), CHAOS(DClass.CHAOS), HS(DClass.HS), HESIOD(DClass.HESIOD), NONE(DClass.NONE), ANY(DClass.ANY);

    private final int type;

    private DnsDclassType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
