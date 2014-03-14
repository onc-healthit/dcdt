package gov.hhs.onc.dcdt.mail.utils;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.utils.ToolRegexUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class ToolMailAddressUtils {
    public final static String MAIL_ADDR_PART_DELIM = "@";

    public final static String MAIL_ADDR_PART_PERSONAL_DELIM = StringUtils.SPACE;
    public final static String MAIL_ADDR_PART_PERSONAL_ADDR_PREFIX = "<";
    public final static String MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX = ">";

    public final static String PATTERN_STR_MAIL_ADDR_PART_PERSONAL_CHAR = "\\w\\-";
    public final static String PATTERN_STR_MAIL_ADDR_PART_PERSONAL_CHAR_ANY = MAIL_ADDR_PART_PERSONAL_DELIM + PATTERN_STR_MAIL_ADDR_PART_PERSONAL_CHAR;
    public final static String PATTERN_STR_MAIL_ADDR_PART_PERSONAL = "(?!" + MAIL_ADDR_PART_PERSONAL_DELIM + ")(["
        + PATTERN_STR_MAIL_ADDR_PART_PERSONAL_CHAR_ANY + "]+?)" + MAIL_ADDR_PART_PERSONAL_DELIM + "+";

    public final static String PATTERN_STR_MAIL_ADDR_PART_LOCAL = "(" + ToolDnsNameUtils.PATTERN_STR_DNS_NAME_REL_BASE + ")(?<=["
        + ToolDnsNameUtils.PATTERN_STR_DNS_NAME_LBL_CHAR_ANY + "]{1,63})";

    public final static String PATTERN_STR_MAIL_ADDR_PART_DOMAIN = "(" + ToolDnsNameUtils.PATTERN_STR_DNS_NAME_REL_BASE + ")(?<=["
        + ToolDnsNameUtils.PATTERN_STR_DNS_NAME_LBL_CHAR_ANY + "]{1,253})";

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
    public final static String PATTERN_STR_MAIL_ADDR_BASE = PATTERN_STR_MAIL_ADDR_PART_LOCAL + MAIL_ADDR_PART_DELIM + PATTERN_STR_MAIL_ADDR_PART_DOMAIN;

    public final static String PATTERN_STR_MAIL_ADDR = "^" + PATTERN_STR_MAIL_ADDR_BASE + "(?<=[" + MAIL_ADDR_PART_DELIM
        + ToolDnsNameUtils.PATTERN_STR_DNS_NAME_LBL_CHAR_ANY + "]{1,256})$";

    public final static String PATTERN_STR_MAIL_ADDR_PERSONAL = "^" + PATTERN_STR_MAIL_ADDR_PART_PERSONAL + MAIL_ADDR_PART_PERSONAL_ADDR_PREFIX
        + PATTERN_STR_MAIL_ADDR_BASE + MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX + "(?<=[" + MAIL_ADDR_PART_DELIM + MAIL_ADDR_PART_PERSONAL_DELIM
        + MAIL_ADDR_PART_PERSONAL_ADDR_PREFIX + MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX + ToolDnsNameUtils.PATTERN_STR_DNS_NAME_LBL_CHAR_ANY + "]{1,256})$";

    public final static String PATTERN_STR_MAIL_ADDR_DIRECT = "^(?:" + PATTERN_STR_MAIL_ADDR_PART_LOCAL + MAIL_ADDR_PART_DELIM + ")?"
        + PATTERN_STR_MAIL_ADDR_PART_DOMAIN + "(?<=[" + MAIL_ADDR_PART_DELIM + ToolDnsNameUtils.PATTERN_STR_DNS_NAME_LBL_CHAR_ANY + "]{1,256})$";

    public final static Pattern PATTERN_MAIL_ADDR = Pattern.compile(PATTERN_STR_MAIL_ADDR);
    public final static Pattern PATTERN_MAIL_ADDR_PERSONAL = Pattern.compile(PATTERN_STR_MAIL_ADDR_PERSONAL);
    public final static Pattern PATTERN_MAIL_ADDR_DIRECT = Pattern.compile(PATTERN_STR_MAIL_ADDR_DIRECT);

    @Nullable
    public static String joinParts(String[] addrParts) {
        if (addrParts.length == 0) {
            return null;
        } else if (addrParts.length == 1) {
            return addrParts[0];
        }

        ToolStrBuilder addrBuilder = new ToolStrBuilder();

        if (addrParts.length >= 3) {
            addrBuilder.appendWithDelimiters(ArrayUtils.subarray(addrParts, 0, (addrParts.length - 2)), MAIL_ADDR_PART_PERSONAL_DELIM);
        }

        if (addrParts.length >= 2) {
            addrBuilder.appendWithDelimiter(addrParts[addrParts.length - 2], MAIL_ADDR_PART_PERSONAL_DELIM);
        }

        return addrBuilder.appendWithDelimiter(addrParts[addrParts.length - 1], MAIL_ADDR_PART_DELIM).build();
    }

    @Nullable
    public static String[] splitParts(@Nullable String addr) {
        if (StringUtils.isBlank(addr)) {
            return null;
        }

        Matcher addrMatcher = getMatcherPersonal(addr);
        addrMatcher = (addrMatcher.matches() ? addrMatcher : getMatcherDirect(addr));

        return ToolRegexUtils.groups(addrMatcher);
    }

    public static boolean isDirectAddress(@Nullable String str) {
        return ((str != null) && getMatcherDirect(str).matches());
    }

    public static boolean isPersonalAddress(@Nullable String str) {
        return ((str != null) && getMatcherPersonal(str).matches());
    }

    public static boolean isAddress(@Nullable String str) {
        return ((str != null) && getMatcher(str).matches());
    }

    public static Matcher getMatcherDirect(String str) {
        return PATTERN_MAIL_ADDR_DIRECT.matcher(str);
    }

    public static Matcher getMatcherPersonal(String str) {
        return PATTERN_MAIL_ADDR_PERSONAL.matcher(str);
    }

    public static Matcher getMatcher(String str) {
        return PATTERN_MAIL_ADDR.matcher(str);
    }
}
