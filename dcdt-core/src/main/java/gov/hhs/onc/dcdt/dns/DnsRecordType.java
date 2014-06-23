package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.Type;

public enum DnsRecordType implements DnsMnemonicIdentifier {
    A(Type.A, ARecord.class), CERT(Type.CERT, CERTRecord.class), CNAME(Type.CNAME, CNAMERecord.class), MX(Type.MX, MXRecord.class),
    NS(Type.NS, NSRecord.class), SOA(Type.SOA, SOARecord.class), SRV(Type.SRV, SRVRecord.class);

    private final int code;
    private final String id;
    private final DnsDclassType dclassType;
    private final Class<? extends Record> recordClass;

    private DnsRecordType(int code, Class<? extends Record> recordClass) {
        this(code, DnsDclassType.IN, recordClass);
    }

    private DnsRecordType(int code, DnsDclassType dclassType, Class<? extends Record> recordClass) {
        this.code = code;
        this.id = Type.string(this.code);
        this.dclassType = dclassType;
        this.recordClass = recordClass;
    }

    @Nonnegative
    @Override
    public int getCode() {
        return this.code;
    }

    public DnsDclassType getDclassType() {
        return this.dclassType;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public Class<? extends Record> getRecordClass() {
        return this.recordClass;
    }
}
