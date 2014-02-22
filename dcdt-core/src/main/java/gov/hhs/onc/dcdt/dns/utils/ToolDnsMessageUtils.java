package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.dns.DnsMessageFlag;
import gov.hhs.onc.dcdt.dns.DnsMessageRcode;
import gov.hhs.onc.dcdt.dns.DnsMessageSection;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.EnumSet;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.xbill.DNS.Message;
import org.xbill.DNS.Record;
import org.xbill.DNS.SOARecord;

public abstract class ToolDnsMessageUtils {
    public static Message createErrorResponse(@Nullable Message reqMsg, DnsMessageRcode rcode) {
        return setRcode(((reqMsg != null) ? createResponse(reqMsg) : new Message()), rcode);
    }
    
    public static Message createResponse(Message reqMsg) {
        Message respMsg = new Message(reqMsg.getHeader().getID());

        if (hasRecords(reqMsg, DnsMessageSection.QUESTION)) {
            setFlags(respMsg, DnsMessageFlag.QR);
            addRecords(respMsg, DnsMessageSection.QUESTION, reqMsg.getQuestion());
        }

        return respMsg;
    }

    public static Message setAuthorities(Message msg, boolean authoritative, @Nullable SOARecord ... authorityRecords) {
        return setAuthorities(msg, authoritative, ToolArrayUtils.asList(authorityRecords));
    }

    public static Message setAuthorities(Message msg, boolean authoritative, @Nullable Iterable<SOARecord> authorityRecords) {
        msg.removeAllRecords(DnsMessageSection.AUTHORITY.getSection());

        if (authorityRecords != null) {
            addRecords(msg, DnsMessageSection.AUTHORITY, authorityRecords);

            if (authoritative && hasRecords(msg, DnsMessageSection.AUTHORITY)) {
                setFlags(msg, DnsMessageFlag.AA);
            }
        }

        return msg;
    }

    public static Message setAnswers(Message msg, @Nullable Record ... answerRecords) {
        return setAnswers(msg, ToolArrayUtils.asList(answerRecords));
    }

    public static Message setAnswers(Message msg, @Nullable Iterable<? extends Record> answerRecords) {
        msg.removeAllRecords(DnsMessageSection.ANSWER.getSection());

        if (answerRecords != null) {
            addRecords(msg, DnsMessageSection.ANSWER, answerRecords);

            if (!hasRecords(msg, DnsMessageSection.ANSWER)) {
                setRcode(msg, DnsMessageRcode.NXDOMAIN);
            }
        }

        return msg;
    }

    public static boolean isError(Message msg) {
        DnsMessageRcode rcode = getRcode(msg);

        return (rcode != null) && rcode.isError();
    }

    @Nullable
    public static DnsMessageRcode getRcode(Message msg) {
        int rcode = msg.getRcode();

        for (DnsMessageRcode enumItem : EnumSet.allOf(DnsMessageRcode.class)) {
            if (enumItem.getRcode() == rcode) {
                return enumItem;
            }
        }

        return null;
    }

    public static Message setRcode(Message msg, @Nullable DnsMessageRcode rcode) {
        msg.getHeader().setRcode(ObjectUtils.defaultIfNull(rcode, DnsMessageRcode.NOERROR).getRcode());

        return msg;
    }

    public static Message addRecords(Message msg, DnsMessageSection section, @Nullable Record ... records) {
        return addRecords(msg, section, ToolArrayUtils.asList(records));
    }

    public static Message addRecords(Message msg, DnsMessageSection section, @Nullable Iterable<? extends Record> records) {
        if (records != null) {
            for (Record record : records) {
                msg.addRecord(record, section.getSection());
            }
        }

        return msg;
    }

    public static boolean hasRecords(Message msg, DnsMessageSection section) {
        return !ArrayUtils.isEmpty(msg.getSectionArray(section.getSection()));
    }

    public static Message setFlags(Message msg, @Nullable DnsMessageFlag ... flags) {
        return setFlags(msg, ToolArrayUtils.asList(flags));
    }

    public static Message setFlags(Message msg, @Nullable Iterable<DnsMessageFlag> flags) {
        if (flags != null) {
            for (DnsMessageFlag flag : flags) {
                msg.getHeader().setFlag(flag.getFlag());
            }
        }

        return msg;
    }
}
