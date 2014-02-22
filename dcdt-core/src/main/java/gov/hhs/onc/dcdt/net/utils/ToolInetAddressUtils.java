package gov.hhs.onc.dcdt.net.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public abstract class ToolInetAddressUtils {
    public final static String PATTERN_STR_IPV4_ADDR_DELIM = "\\.";
    public final static String PATTERN_STR_IPV4_ADDR_GROUP = "(?:25[0-5]|2[0-4]\\d|[0-1]\\d{2}|\\d{1,2})";
    public final static String PATTERN_STR_IPV4_ADDR = "^(?:" + PATTERN_STR_IPV4_ADDR_GROUP + PATTERN_STR_IPV4_ADDR_DELIM + "){3}"
        + PATTERN_STR_IPV4_ADDR_GROUP + "$";

    public final static Pattern PATTERN_IPV4_ADDR = Pattern.compile(PATTERN_STR_IPV4_ADDR);

    public static InetAddress getByAddress(String hostAddrStr) throws UnknownHostException {
        return getByAddress(null, hostAddrStr);
    }

    public static InetAddress getByAddress(@Nullable String hostName, String hostAddrStr) throws UnknownHostException {
        return InetAddress.getByAddress(hostName, addressStringToBytes(hostAddrStr));
    }

    public static byte[] addressStringToBytes(String addrStr) throws UnknownHostException {
        if (!isIpv4Address(addrStr)) {
            throw new UnknownHostException(String.format("Invalid address string: %s", addrStr));
        }

        String[] addrPartStrs = addrStr.split(PATTERN_STR_IPV4_ADDR_DELIM, 4);
        byte[] addrBytes = new byte[4];

        for (int a = 0; a < addrPartStrs.length; a++) {
            addrBytes[a] = Byte.valueOf(addrPartStrs[a]);
        }

        return addrBytes;
    }

    public static boolean isIpv4Address(String addrStr) {
        return getIpv4AddressMatcher(addrStr).matches();
    }

    public static Matcher getIpv4AddressMatcher(String addrStr) {
        return PATTERN_IPV4_ADDR.matcher(addrStr);
    }
}
