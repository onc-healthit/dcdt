package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.xbill.DNS.A6Record;
import org.xbill.DNS.AAAARecord;
import org.xbill.DNS.AFSDBRecord;
import org.xbill.DNS.APLRecord;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.DHCIDRecord;
import org.xbill.DNS.DLVRecord;
import org.xbill.DNS.DNAMERecord;
import org.xbill.DNS.DNSKEYRecord;
import org.xbill.DNS.DSRecord;
import org.xbill.DNS.GPOSRecord;
import org.xbill.DNS.HINFORecord;
import org.xbill.DNS.IPSECKEYRecord;
import org.xbill.DNS.ISDNRecord;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KXRecord;
import org.xbill.DNS.LOCRecord;
import org.xbill.DNS.MBRecord;
import org.xbill.DNS.MDRecord;
import org.xbill.DNS.MFRecord;
import org.xbill.DNS.MGRecord;
import org.xbill.DNS.MINFORecord;
import org.xbill.DNS.MRRecord;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.NSAPRecord;
import org.xbill.DNS.NSAP_PTRRecord;
import org.xbill.DNS.NSEC3PARAMRecord;
import org.xbill.DNS.NSEC3Record;
import org.xbill.DNS.NSECRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.NULLRecord;
import org.xbill.DNS.NXTRecord;
import org.xbill.DNS.OPTRecord;
import org.xbill.DNS.PTRRecord;
import org.xbill.DNS.PXRecord;
import org.xbill.DNS.RPRecord;
import org.xbill.DNS.RRSIGRecord;
import org.xbill.DNS.RTRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.SIGRecord;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SPFRecord;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.SSHFPRecord;
import org.xbill.DNS.TKEYRecord;
import org.xbill.DNS.TLSARecord;
import org.xbill.DNS.TSIGRecord;
import org.xbill.DNS.TXTRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord;
import org.xbill.DNS.X25Record;

public enum DnsRecordType implements DnsMnemonicIdentifier {
    A(Type.A, ARecord.class, true), NS(Type.NS, NSRecord.class, true), MD(Type.MD, MDRecord.class), MF(Type.MF, MFRecord.class), CNAME(Type.CNAME,
        CNAMERecord.class, true), SOA(Type.SOA, SOARecord.class, true), MB(Type.MB, MBRecord.class), MG(Type.MG, MGRecord.class), MR(Type.MR, MRRecord.class),
    NULL(Type.NULL, NULLRecord.class), WKS(Type.WKS, WKSRecord.class), PTR(Type.PTR, PTRRecord.class, true), HINFO(Type.HINFO, HINFORecord.class), MINFO(
        Type.MINFO, MINFORecord.class), MX(Type.MX, MXRecord.class, true), TXT(Type.TXT, TXTRecord.class, true), RP(Type.RP, RPRecord.class), AFSDB(Type.AFSDB,
        AFSDBRecord.class), X25(Type.X25, X25Record.class), ISDN(Type.ISDN, ISDNRecord.class), RT(Type.RT, RTRecord.class), NSAP(Type.NSAP, NSAPRecord.class),
    NSAP_PTR(Type.NSAP_PTR, NSAP_PTRRecord.class), SIG(Type.SIG, SIGRecord.class), KEY(Type.KEY, KEYRecord.class), PX(Type.PX, PXRecord.class), GPOS(Type.GPOS,
        GPOSRecord.class), AAAA(Type.AAAA, AAAARecord.class), LOC(Type.LOC, LOCRecord.class), NXT(Type.NXT, NXTRecord.class), EID(Type.EID),
    NIMLOC(Type.NIMLOC), SRV(Type.SRV, SRVRecord.class, true), ATMA(Type.ATMA), NAPTR(Type.NAPTR, NAPTRRecord.class), KX(Type.KX, KXRecord.class), CERT(
        Type.CERT, CERTRecord.class, true), A6(Type.A6, A6Record.class), DNAME(Type.DNAME, DNAMERecord.class), OPT(Type.OPT, OPTRecord.class), APL(Type.APL,
        APLRecord.class), DS(Type.DS, DSRecord.class), SSHFP(Type.SSHFP, SSHFPRecord.class), IPSECKEY(Type.IPSECKEY, IPSECKEYRecord.class), RRSIG(Type.RRSIG,
        RRSIGRecord.class), NSEC(Type.NSEC, NSECRecord.class), DNSKEY(Type.DNSKEY, DNSKEYRecord.class), DHCID(Type.DHCID, DHCIDRecord.class), NSEC3(Type.NSEC3,
        NSEC3Record.class), NSEC3PARAM(Type.NSEC3PARAM, NSEC3PARAMRecord.class), TLSA(Type.TLSA, TLSARecord.class), SPF(Type.SPF, SPFRecord.class), TKEY(
        Type.TKEY, TKEYRecord.class), TSIG(Type.TSIG, TSIGRecord.class), IXFR(Type.IXFR), AXFR(Type.AXFR), MAILB(Type.MAILB), MAILA(Type.MAILA), ANY(Type.ANY),
    DLV(Type.DLV, DLVRecord.class);

    private final int code;
    private final Class<? extends Record> recordClass;
    private final boolean processed;
    private final String id;
    private final DnsDclassType dclassType;

    private DnsRecordType(@Nonnegative int code) {
        this(code, null);
    }

    private DnsRecordType(@Nonnegative int code, @Nullable Class<? extends Record> recordClass) {
        this(code, recordClass, false);
    }

    private DnsRecordType(@Nonnegative int code, @Nullable Class<? extends Record> recordClass, boolean processed) {
        this.code = code;
        this.recordClass = recordClass;
        this.processed = processed;
        this.id = Type.string(this.code);
        this.dclassType = (Type.isRR(this.code) ? DnsDclassType.IN : DnsDclassType.NONE);
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

    public boolean isProcessed() {
        return processed;
    }

    public boolean hasRecordClass() {
        return (this.recordClass != null);
    }

    @Nullable
    public Class<? extends Record> getRecordClass() {
        return this.recordClass;
    }
}
