package gov.hhs.onc.dcdt.net.utils;

import gov.hhs.onc.dcdt.utils.ToolRegexUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public abstract class ToolInetAddressUtils {
    public final static String IPV4_ADDR_DELIM = ".";

    public final static String PATTERN_STR_IPV4_ADDR_DELIM = "\\" + IPV4_ADDR_DELIM;
    public final static String PATTERN_STR_IPV4_ADDR_OCTET = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|\\d{1,2})";
    public final static String PATTERN_STR_IPV4_ADDR = "^" + PATTERN_STR_IPV4_ADDR_OCTET + PATTERN_STR_IPV4_ADDR_DELIM + PATTERN_STR_IPV4_ADDR_OCTET
        + PATTERN_STR_IPV4_ADDR_DELIM + PATTERN_STR_IPV4_ADDR_OCTET + PATTERN_STR_IPV4_ADDR_DELIM + PATTERN_STR_IPV4_ADDR_OCTET + "$";

    public final static Pattern PATTERN_IPV4_ADDR = Pattern.compile(PATTERN_STR_IPV4_ADDR);

    public static InetAddress getConnectionAddress(InetAddress addr) {
        return addr.isAnyLocalAddress() ? InetAddress.getLoopbackAddress() : addr;
    }

    @Nullable
    public static InetAddress getByAddress(@Nullable String str) throws UnknownHostException {
        return (!StringUtils.isBlank(str) ? (isAddress(str) ? getByAddress(null, str) : getByName(str)) : null);
    }

    @Nullable
    public static InetAddress getByAddress(@Nullable String hostName, String addr) throws UnknownHostException {
        return (isAddress(addr) ? InetAddress.getByAddress(hostName, getOctetBytes(addr)) : null);
    }

    @Nullable
    public static InetAddress getByName(@Nullable String hostName) throws UnknownHostException {
        return (!StringUtils.isBlank(hostName) ? InetAddress.getByName(hostName) : null);
    }

    @Nullable
    public static byte[] getOctetBytes(@Nullable String addr) {
        String[] octets = getOctets(addr);

        if (octets == null) {
            return null;
        }

        byte[] octetBytes = new byte[octets.length];

        for (int a = 0; a < octetBytes.length; a++) {
            octetBytes[a] = ((byte) Integer.parseInt(octets[a]));
        }

        return octetBytes;
    }

    @Nullable
    public static String[] getOctets(@Nullable String addr) {
        return ToolRegexUtils.groups(getMatcher(addr));
    }

    public static boolean isAddress(@Nullable String addr) {
        return ((addr != null) && getMatcher(addr).matches());
    }

    public static Matcher getMatcher(String addr) {
        return PATTERN_IPV4_ADDR.matcher(addr);
    }
}
