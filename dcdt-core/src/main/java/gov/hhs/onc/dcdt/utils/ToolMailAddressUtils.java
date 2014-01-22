package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.dns.DnsNameException;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.Name;

public abstract class ToolMailAddressUtils {
    private final static String PART_DELIM = "@";

    public static String getLocal(String mailAddr) {
        return ToolArrayUtils.getFirst(getParts(mailAddr));
    }

    public static Name getDomainName(String mailAddr) throws DnsNameException {
        return ToolDnsNameUtils.fromString(getDomainNameString(mailAddr));
    }

    public static String getDomainNameString(String mailAddr) {
        return ToolArrayUtils.getLast(getParts(mailAddr));
    }

    public static String[] getParts(String mailAddr) {
        return hasLocal(mailAddr) ? StringUtils.split(mailAddr, PART_DELIM, 2) : ArrayUtils.toArray(null, mailAddr);
    }

    public static boolean hasLocal(String mailAddr) {
        return StringUtils.contains(mailAddr, PART_DELIM);
    }
}
