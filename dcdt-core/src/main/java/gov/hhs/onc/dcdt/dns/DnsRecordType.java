package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.Type;

public enum DnsRecordType {
    A(Type.A, ARecord.class), CERT(Type.CERT, CERTRecord.class), CNAME(Type.CNAME, CNAMERecord.class), MX(Type.MX, MXRecord.class), NS(Type.NS, NSRecord.class), SOA(
        Type.SOA, SOARecord.class), SRV(Type.SRV, SRVRecord.class);

    private final int type;
    private final String typeDisplay;
    private final int dclass;
    private final Class<? extends Record> recordClass;

    private DnsRecordType(int type, Class<? extends Record> recordClass) {
        this.type = type;
        this.typeDisplay = Type.string(this.type);
        this.dclass = DClass.IN;
        this.recordClass = recordClass;
    }

    public int getDclass() {
        return this.dclass;
    }

    public Class<? extends Record> getRecordClass() {
        return this.recordClass;
    }

    public int getType() {
        return this.type;
    }

    public String getTypeDisplay() {
        return this.typeDisplay;
    }
}
