package gov.hhs.onc.dcdt.mail.utils;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class ToolMailAddressUtils {
    public final static String DELIM_MAIL_ADDR_PARTS = "@";

    @Nullable
    public static String joinParts(String[] mailAddrParts) {
        return StringUtils.stripToNull(ToolStringUtils.joinDelimit(ArrayUtils.nullToEmpty(mailAddrParts), DELIM_MAIL_ADDR_PARTS));
    }

    public static String[] splitParts(@Nullable String mailAddr) {
        String[] mailAddrParts = StringUtils.split(mailAddr, DELIM_MAIL_ADDR_PARTS, 2);

        return ToolArrayUtils.emptyToNull((ArrayUtils.getLength(mailAddrParts) == 2) ? mailAddrParts : ArrayUtils.add(mailAddrParts, 0, StringUtils.EMPTY));
    }
}
