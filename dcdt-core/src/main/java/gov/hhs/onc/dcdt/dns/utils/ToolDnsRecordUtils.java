package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.dns.DnsCertificateType;
import gov.hhs.onc.dcdt.dns.DnsDclassType;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithmType;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsSpfStrings;
import gov.hhs.onc.dcdt.dns.config.DnsRecordConfig;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnegative;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.DNSKEYRecord;
import org.xbill.DNS.DNSKEYRecord.Protocol;
import org.xbill.DNS.DNSSEC.DNSSECException;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;

public abstract class ToolDnsRecordUtils {
    public static boolean hasCertRecordParameter(CERTRecord certRecord, DnsCertificateType certType) {
        return hasCertRecordParameter(certRecord, certType, null);
    }

    public static boolean hasCertRecordParameter(CERTRecord certRecord, DnsCertificateType certType, Set<DnsKeyAlgorithmType> keyAlgTypes) {
        return ((keyAlgTypes == null) || keyAlgTypes.contains(ToolDnsUtils.findByCode(DnsKeyAlgorithmType.class, certRecord.getAlgorithm())))
            && ((certType == null) || (certRecord.getCertType() == certType.getCode()));
    }

    public static String transformDnsRecordDataToString(Record record) {
        return record.rdataToString();
    }

    public static Name transformDnsRecordTarget(Record record) {
        // noinspection ConstantConditions
        switch (ToolDnsUtils.findByCode(DnsRecordType.class, record.getType())) {
            case CNAME:
                return ((CNAMERecord) record).getTarget();

            case MX:
                return ((MXRecord) record).getTarget();

            case NS:
                return ((NSRecord) record).getTarget();

            case SRV:
                return ((SRVRecord) record).getTarget();

            default:
                return null;
        }
    }

    public static Record transformDnsRecordConfig(DnsRecordConfig<? extends Record> recordConfig) throws DnsException {
        return recordConfig.toRecord();
    }

    public final static DateFormat DATE_FORMAT_SERIAL = new SimpleDateFormat("yyyyMMdd");

    public static String buildSpf(String ... spfStrs) {
        return buildSpf(ToolArrayUtils.asList(spfStrs));
    }

    public static String buildSpf(List<String> spfStrs) {
        if (!Objects.equals(ToolListUtils.getFirst(spfStrs), DnsSpfStrings.MOD_VERSION_1)) {
            ToolListUtils.addFirst(spfStrs, DnsSpfStrings.MOD_VERSION_1);
        }

        if (!Objects.equals(ToolListUtils.getLast(spfStrs), DnsSpfStrings.MECH_ALL_FAIL)) {
            spfStrs.add(DnsSpfStrings.MECH_ALL_FAIL);
        }

        // noinspection ConstantConditions
        return ToolStringUtils.joinDelimit(spfStrs, DnsSpfStrings.DELIM);
    }

    @Nonnegative
    public static int generateSerial() {
        return generateSerial(0);
    }

    @Nonnegative
    public static int generateSerial(@Nonnegative int seqNum) {
        return Integer.parseInt(DATE_FORMAT_SERIAL.format(new Date()) + StringUtils.leftPad(Integer.toString(seqNum), 2, Integer.toString(0)));
    }

    @Nonnegative
    public static int getKeyTag(DnsKeyAlgorithmType keyAlgType, PublicKey publicKey) throws DnsException {
        try {
            return new DNSKEYRecord(Name.root, DnsDclassType.IN.getCode(), 0, 0, Protocol.DNSSEC, keyAlgType.getCode(), publicKey).getFootprint();
        } catch (DNSSECException e) {
            throw new DnsException(String.format("Unable to get key tag for public key (class=%s, algType=%s).", ToolClassUtils.getName(publicKey),
                keyAlgType.name()), e);
        }
    }
}
