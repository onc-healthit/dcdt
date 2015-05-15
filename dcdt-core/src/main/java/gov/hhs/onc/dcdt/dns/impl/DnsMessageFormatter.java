package gov.hhs.onc.dcdt.dns.impl;

import gov.hhs.onc.dcdt.dns.DnsCertificateType;
import gov.hhs.onc.dcdt.dns.DnsDclassType;
import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithmType;
import gov.hhs.onc.dcdt.dns.DnsMessageFlag;
import gov.hhs.onc.dcdt.dns.DnsMessageOpcode;
import gov.hhs.onc.dcdt.dns.DnsMessageSection;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsMessageUtils;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsUtils;
import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.Header;
import org.xbill.DNS.Message;
import org.xbill.DNS.Record;
import org.xbill.DNS.TTL;

@Component("formatterDnsMsg")
public class DnsMessageFormatter extends AbstractToolFormatter<Message> {
    private final static int WIDTH_TAB = 8;

    private final static String PREFIX_SEC = ";; ";
    private final static String PREFIX_SEC_ITEM_VALUE_ARR = "[";

    private final static String SUFFIX_SEC_NAME = ": ";
    private final static String SUFFIX_SEC_ITEM_VALUE_ARR = "]";

    private final static String DELIM_SEC = ", ";
    private final static String DELIM_SEC_ITEM = "=";
    private final static String DELIM_SEC_ITEM_VALUE = StringUtils.SPACE;
    private final static String DELIM_RECORD = "\t";
    private final static String DELIM_RECORD_DATA = StringUtils.SPACE;

    private final static String SEC_NAME_HEADER = "HEADER";
    private final static String SEC_NAME_TSIG = "TSIG";

    private final static String SEC_ITEM_NAME_HEADER_OP = "op";
    private final static String SEC_ITEM_NAME_HEADER_FLAGS = "flags";
    private final static String SEC_ITEM_NAME_HEADER_SIZE = "size";
    private final static String SEC_ITEM_NAME_HEADER_ID = "id";
    private final static String SEC_ITEM_NAME_HEADER_STATUS = "status";

    private final static String SEC_ITEM_NAME_TSIG_VERIFIED = "verified";

    public DnsMessageFormatter() {
        super(Message.class, false, true);
    }

    @Override
    protected String printInternal(Message obj, Locale locale) throws Exception {
        ToolStrBuilder strBuilder = new ToolStrBuilder();

        Header header = obj.getHeader();
        strBuilder.append(PREFIX_SEC);
        strBuilder.append(SEC_NAME_HEADER);
        strBuilder.append(SUFFIX_SEC_NAME);

        DnsMessageOpcode op = ToolDnsMessageUtils.getOpcode(obj);
        strBuilder.append(SEC_ITEM_NAME_HEADER_OP);
        strBuilder.append(DELIM_SEC_ITEM);
        // noinspection ConstantConditions
        strBuilder.append(op.getId());

        strBuilder.append(DELIM_SEC);
        strBuilder.append(SEC_ITEM_NAME_HEADER_FLAGS);
        strBuilder.append(DELIM_SEC_ITEM);
        strBuilder.append(PREFIX_SEC_ITEM_VALUE_ARR);

        Set<DnsMessageFlag> flags = ToolDnsMessageUtils.getFlags(obj);

        if (!flags.isEmpty()) {
            for (DnsMessageFlag flag : flags) {
                strBuilder.append(DELIM_SEC_ITEM_VALUE);
                strBuilder.append(flag.getId());
            }

            strBuilder.append(DELIM_SEC_ITEM_VALUE);
        }

        strBuilder.append(SUFFIX_SEC_ITEM_VALUE_ARR);

        strBuilder.append(DELIM_SEC);
        strBuilder.append(SEC_ITEM_NAME_HEADER_SIZE);
        strBuilder.append(DELIM_SEC_ITEM);
        strBuilder.append(obj.numBytes());

        strBuilder.append(DELIM_SEC);
        strBuilder.append(SEC_ITEM_NAME_HEADER_ID);
        strBuilder.append(DELIM_SEC_ITEM);
        strBuilder.append(header.getID());

        strBuilder.append(DELIM_SEC);
        strBuilder.append(SEC_ITEM_NAME_HEADER_STATUS);
        strBuilder.append(DELIM_SEC_ITEM);
        // noinspection ConstantConditions
        strBuilder.append(ToolDnsUtils.getId(ToolDnsMessageUtils.getRcode(obj), header.getRcode()));

        if (obj.isSigned()) {
            printTsigSection(strBuilder, obj.isVerified());
        }

        int secCode;

        for (DnsMessageSection sec : EnumSet.allOf(DnsMessageSection.class)) {
            if (header.getCount((secCode = sec.getCode())) == 0) {
                continue;
            }

            strBuilder.appendNewLine();
            strBuilder.append(PREFIX_SEC);
            strBuilder.append(((op != DnsMessageOpcode.UPDATE) ? sec.getIdDisplay() : sec.getIdUpdateDisplay()));

            for (Record record : obj.getSectionArray(secCode)) {
                printRecord(strBuilder, sec, record);
            }
        }

        return strBuilder.build();
    }

    private static void printRecord(ToolStrBuilder strBuilder, DnsMessageSection sec, Record record) {
        int recordTypeCode = record.getType();
        DnsRecordType recordType = ToolDnsUtils.findByCode(DnsRecordType.class, recordTypeCode);

        printRecordInfo(strBuilder, sec, recordTypeCode, recordType, record);

        if (sec != DnsMessageSection.QUESTION) {
            strBuilder.append(DELIM_RECORD);

            if (recordType == DnsRecordType.CERT) {
                printCertRecordData(strBuilder, ((CERTRecord) record));
            } else {
                printRecordData(strBuilder, record);
            }
        }
    }

    private static void printCertRecordData(ToolStrBuilder strBuilder, CERTRecord certRecord) {
        int certTypeCode = certRecord.getCertType();
        // noinspection ConstantConditions
        strBuilder.append(ToolDnsUtils.getId(ToolDnsUtils.findByCode(DnsCertificateType.class, certTypeCode), certTypeCode));

        strBuilder.append(DELIM_RECORD_DATA);
        strBuilder.append(certRecord.getKeyTag());

        int keyAlgTypeCode = certRecord.getAlgorithm();
        strBuilder.append(DELIM_RECORD_DATA);
        // noinspection ConstantConditions
        strBuilder.append(ToolDnsUtils.getId(ToolDnsUtils.findByCode(DnsKeyAlgorithmType.class, keyAlgTypeCode), keyAlgTypeCode));

        strBuilder.appendWithDelimiter(Base64.encodeBase64String(certRecord.getCert()), DELIM_RECORD_DATA);
    }

    private static void printRecordData(ToolStrBuilder strBuilder, Record record) {
        strBuilder.append(record.rdataToString());
    }

    private static void printRecordInfo(ToolStrBuilder strBuilder, DnsMessageSection sec, int recordTypeCode, DnsRecordType recordType, Record record) {
        strBuilder.appendNewLine();

        String nameStr = record.getName().toString();
        strBuilder.append(nameStr);

        int nameStrLen = nameStr.length();

        if (nameStrLen < WIDTH_TAB) {
            strBuilder.append(DELIM_RECORD);
        }

        if (nameStrLen < (WIDTH_TAB * 2)) {
            strBuilder.append(DELIM_RECORD);
        }

        strBuilder.append(DELIM_RECORD);

        if (sec != DnsMessageSection.QUESTION) {
            strBuilder.append(TTL.format(record.getTTL()));
        }

        int dclassTypeCode = record.getDClass();
        strBuilder.append(DELIM_RECORD);
        // noinspection ConstantConditions
        strBuilder.append(ToolDnsUtils.getId(ToolDnsUtils.findByCode(DnsDclassType.class, dclassTypeCode), dclassTypeCode));
        strBuilder.append(DELIM_RECORD);
        strBuilder.append(ToolDnsUtils.getId(recordType, recordTypeCode));
    }

    private static void printTsigSection(ToolStrBuilder strBuilder, boolean verified) {
        strBuilder.appendNewLine();
        strBuilder.append(PREFIX_SEC);
        strBuilder.append(SEC_NAME_TSIG);
        strBuilder.append(SUFFIX_SEC_NAME);

        strBuilder.append(SEC_ITEM_NAME_TSIG_VERIFIED);
        strBuilder.append(DELIM_SEC_ITEM);
        strBuilder.append(verified);
    }
}
