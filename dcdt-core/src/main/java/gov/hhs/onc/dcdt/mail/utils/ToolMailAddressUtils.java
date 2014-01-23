package gov.hhs.onc.dcdt.mail.utils;

import gov.hhs.onc.dcdt.dns.DnsNameException;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.Name;

public abstract class ToolMailAddressUtils {
    public final static String DELIM_ADDR = "@";

    public static boolean hasLocalPart(String mailAddr) {
        return StringUtils.contains(mailAddr, DELIM_ADDR);
    }

    @Nullable
    public static String getLocalPart(String mailAddr) {
        return ToolArrayUtils.getFirst(getParts(mailAddr));
    }

    public static Name getDomainName(String mailAddr) throws DnsNameException {
        return ToolDnsNameUtils.fromString(getDomainNameString(mailAddr));
    }

    @Nullable
    public static String getDomainNameString(String mailAddr) {
        return ToolArrayUtils.getLast(getParts(mailAddr));
    }

    public static String[] getParts(String mailAddr) {
        return hasLocalPart(mailAddr) ? StringUtils.split(mailAddr, DELIM_ADDR, 2) : ArrayUtils.toArray(null, mailAddr);
    }
}
