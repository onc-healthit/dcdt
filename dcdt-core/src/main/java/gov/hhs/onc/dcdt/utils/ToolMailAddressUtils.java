package gov.hhs.onc.dcdt.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class ToolMailAddressUtils {
    private final static String PART_DELIM = "@";

    public static String getLocal(String mailAddr) {
        return ToolArrayUtils.getFirst(getParts(mailAddr));
    }

    public static String getDomain(String mailAddr) {
        return ToolArrayUtils.getLast(getParts(mailAddr));
    }

    public static String[] getParts(String mailAddr) {
        return hasLocal(mailAddr) ? StringUtils.split(mailAddr, PART_DELIM, 2) : ArrayUtils.toArray(null, mailAddr);
    }

    public static boolean hasLocal(String mailAddr) {
        return StringUtils.contains(mailAddr, PART_DELIM);
    }
}
