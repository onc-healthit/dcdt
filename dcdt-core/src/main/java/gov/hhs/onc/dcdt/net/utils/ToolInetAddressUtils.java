package gov.hhs.onc.dcdt.net.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public abstract class ToolInetAddressUtils {
    public final static String IPV4_ADDR_DELIM = ".";
    public final static String IPV4_ADDR_PATTERN_STR = "^(?:(?:25[0-5]|2[0-4]\\d|[0-1]\\d{0,2})\\.){3}(?:25[0-5]|2[0-4]\\d|[0-1]\\d{0,2})$";
    public final static Pattern IPV4_ADDR_PATTERN = Pattern.compile(IPV4_ADDR_PATTERN_STR);

    public static InetAddress getByAddress(String hostName, String hostAddrStr) throws UnknownHostException {
        return InetAddress.getByAddress(hostName, addressStringToBytes(hostAddrStr));
    }

    public static byte[] addressStringToBytes(String addrStr) throws UnknownHostException {
        if (!isIpv4Address(addrStr)) {
            throw new UnknownHostException(String.format("Invalid address string: %s", addrStr));
        }

        String[] addrPartStrs = StringUtils.split(addrStr, IPV4_ADDR_DELIM, 4);
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
        return IPV4_ADDR_PATTERN.matcher(addrStr);
    }
}
