package gov.hhs.onc.dcdt.mail.utils;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class ToolMailAddressUtils {
    public final static String DELIM_MAIL_ADDR_PARTS = "@";

    public final static String PATTERN_STR_MAIL_ADDR_LOCAL_PART = ToolDnsNameUtils.PATTERN_STR_DNS_NAME_NOT_ABS;
    public final static String PATTERN_STR_MAIL_ADDR_DOMAIN_PART = ToolDnsNameUtils.PATTERN_STR_DNS_NAME_NOT_ABS;
    public final static String PATTERN_STR_MAIL_ADDR_LEN = "(?<=.*{3,253})";

    /**
     * Derived from the rules defined in:
     * <ul>
     * <li><a href="http://tools.ietf.org/html/rfc1034">RFC 1034 - Domain Concepts and Facilities</a></li>
     * <li><a href="http://tools.ietf.org/html/rfc1035">RFC 1035 - Domain Implementation and Specification</a></li>
     * <li><a href="http://tools.ietf.org/html/rfc5321">RFC 5321 - SMTP</a></li>
     * <li><a href="http://tools.ietf.org/html/rfc5322">RFC 5322 - Internet Message Format</a></li>
     * </ul>
     * 
     * A summary of the rules is available here: <a href="http://en.wikipedia.org/wiki/Email_address">Email_address</a>
     * 
     * @see gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils#PATTERN_STR_DNS_NAME
     */
    public final static String PATTERN_STR_MAIL_ADDR = PATTERN_STR_MAIL_ADDR_LOCAL_PART + DELIM_MAIL_ADDR_PARTS + PATTERN_STR_MAIL_ADDR_DOMAIN_PART
        + PATTERN_STR_MAIL_ADDR_LEN;
    public final static String PATTERN_STR_DIRECT_ADDR = PATTERN_STR_MAIL_ADDR;

    @Nullable
    public static String joinParts(String[] mailAddrParts) {
        return StringUtils.stripToNull(ToolStringUtils.joinDelimit(ArrayUtils.nullToEmpty(mailAddrParts), DELIM_MAIL_ADDR_PARTS));
    }

    public static String[] splitParts(@Nullable String mailAddr) {
        String[] mailAddrParts = StringUtils.split(mailAddr, DELIM_MAIL_ADDR_PARTS, 2);

        return ToolArrayUtils.emptyToNull((ArrayUtils.getLength(mailAddrParts) == 2) ? mailAddrParts : ArrayUtils.add(mailAddrParts, 0, StringUtils.EMPTY));
    }
}
