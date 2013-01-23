package gov.hhs.onc.dcdt.dns;

import org.apache.commons.lang3.StringUtils;
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
import org.xbill.DNS.TSIGRecord;
import org.xbill.DNS.TXTRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord;
import org.xbill.DNS.X25Record;

/**
 * @see org.xbill.DNS.Type
 */
public class RecordType<T extends Record>
{
	public final static RecordType<ARecord> A = new RecordType<>(Type.A, ARecord.class);
	public final static RecordType<NSRecord> NS = new RecordType<>(Type.NS, NSRecord.class);
	public final static RecordType<MDRecord> MD = new RecordType<>(Type.MD, MDRecord.class);
	public final static RecordType<MFRecord> MF = new RecordType<>(Type.MF, MFRecord.class);
	public final static RecordType<CNAMERecord> CNAME = new RecordType<>(Type.CNAME, CNAMERecord.class);
	public final static RecordType<SOARecord> SOA = new RecordType<>(Type.SOA, SOARecord.class);
	public final static RecordType<MBRecord> MB = new RecordType<>(Type.MB, MBRecord.class);
	public final static RecordType<MGRecord> MG = new RecordType<>(Type.MG, MGRecord.class);
	public final static RecordType<MRRecord> MR = new RecordType<>(Type.MR, MRRecord.class);
	public final static RecordType<NULLRecord> NULL = new RecordType<>(Type.NULL, NULLRecord.class);
	public final static RecordType<WKSRecord> WKS = new RecordType<>(Type.WKS, WKSRecord.class);
	public final static RecordType<PTRRecord> PTR = new RecordType<>(Type.PTR, PTRRecord.class);
	public final static RecordType<HINFORecord> HINFO = new RecordType<>(Type.HINFO, HINFORecord.class);
	public final static RecordType<MINFORecord> MINFO = new RecordType<>(Type.MINFO, MINFORecord.class);
	public final static RecordType<MXRecord> MX = new RecordType<>(Type.MX, MXRecord.class);
	public final static RecordType<TXTRecord> TXT = new RecordType<>(Type.TXT, TXTRecord.class);
	public final static RecordType<RPRecord> RP = new RecordType<>(Type.RP, RPRecord.class);
	public final static RecordType<AFSDBRecord> AFSDB = new RecordType<>(Type.AFSDB, AFSDBRecord.class);
	public final static RecordType<X25Record> X25 = new RecordType<>(Type.X25, X25Record.class);
	public final static RecordType<ISDNRecord> ISDN = new RecordType<>(Type.ISDN, ISDNRecord.class);
	public final static RecordType<RTRecord> RT = new RecordType<>(Type.RT, RTRecord.class);
	public final static RecordType<NSAPRecord> NSAP = new RecordType<>(Type.NSAP, NSAPRecord.class);
	public final static RecordType<NSAP_PTRRecord> NSAP_PTR = new RecordType<>(Type.NSAP_PTR, NSAP_PTRRecord.class);
	public final static RecordType<SIGRecord> SIG = new RecordType<>(Type.SIG, SIGRecord.class);
	public final static RecordType<KEYRecord> KEY = new RecordType<>(Type.KEY, KEYRecord.class);
	public final static RecordType<PXRecord> PX = new RecordType<>(Type.PX, PXRecord.class);
	public final static RecordType<GPOSRecord> GPOS = new RecordType<>(Type.GPOS, GPOSRecord.class);
	public final static RecordType<AAAARecord> AAAA = new RecordType<>(Type.AAAA, AAAARecord.class);
	public final static RecordType<LOCRecord> LOC = new RecordType<>(Type.LOC, LOCRecord.class);
	public final static RecordType<NXTRecord> NXT = new RecordType<>(Type.NXT, NXTRecord.class);
	public final static RecordType<SRVRecord> SRV = new RecordType<>(Type.SRV, SRVRecord.class);
	public final static RecordType<NAPTRRecord> NAPTR = new RecordType<>(Type.NAPTR, NAPTRRecord.class);
	public final static RecordType<KXRecord> KX = new RecordType<>(Type.KX, KXRecord.class);
	public final static RecordType<CERTRecord> CERT = new RecordType<>(Type.CERT, CERTRecord.class);
	public final static RecordType<A6Record> A6 = new RecordType<>(Type.A6, A6Record.class);
	public final static RecordType<DNAMERecord> DNAME = new RecordType<>(Type.DNAME, DNAMERecord.class);
	public final static RecordType<OPTRecord> OPT = new RecordType<>(Type.OPT, OPTRecord.class);
	public final static RecordType<APLRecord> APL = new RecordType<>(Type.APL, APLRecord.class);
	public final static RecordType<DSRecord> DS = new RecordType<>(Type.DS, DSRecord.class);
	public final static RecordType<SSHFPRecord> SSHFP = new RecordType<>(Type.SSHFP, SSHFPRecord.class);
	public final static RecordType<IPSECKEYRecord> IPSECKEY = new RecordType<>(Type.IPSECKEY, IPSECKEYRecord.class);
	public final static RecordType<RRSIGRecord> RRSIG = new RecordType<>(Type.RRSIG, RRSIGRecord.class);
	public final static RecordType<NSECRecord> NSEC = new RecordType<>(Type.NSEC, NSECRecord.class);
	public final static RecordType<DNSKEYRecord> DNSKEY = new RecordType<>(Type.DNSKEY, DNSKEYRecord.class);
	public final static RecordType<DHCIDRecord> DHCID = new RecordType<>(Type.DHCID, DHCIDRecord.class);
	public final static RecordType<NSEC3Record> NSEC3 = new RecordType<>(Type.NSEC3, NSEC3Record.class);
	public final static RecordType<NSEC3PARAMRecord> NSEC3PARAM = new RecordType<>(Type.NSEC3PARAM, NSEC3PARAMRecord.class);
	public final static RecordType<SPFRecord> SPF = new RecordType<>(Type.SPF, SPFRecord.class);
	public final static RecordType<TKEYRecord> TKEY = new RecordType<>(Type.TKEY, TKEYRecord.class);
	public final static RecordType<TSIGRecord> TSIG = new RecordType<>(Type.TSIG, TSIGRecord.class);
	public final static RecordType<DLVRecord> DLV = new RecordType<>(Type.DLV, DLVRecord.class);
	
	private int type;
	private Class<T> recordClass;
	
	RecordType(int type, Class<T> recordClass)
	{
		this.type = type;
		this.recordClass = recordClass;
	}

	public boolean hasName()
	{
		return !StringUtils.isBlank(this.getName());
	}
	
	public String getName()
	{
		return Type.string(this.type);
	}

	public boolean hasRecordClass()
	{
		return this.recordClass != null;
	}
	
	public Class<T> getRecordClass()
	{
		return this.recordClass;
	}

	public boolean hasType()
	{
		return Type.isRR(this.type);
	}
	
	public int getType()
	{
		return this.type;
	}
}