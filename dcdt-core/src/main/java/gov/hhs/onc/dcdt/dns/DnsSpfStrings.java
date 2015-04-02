package gov.hhs.onc.dcdt.dns;

import org.apache.commons.lang3.StringUtils;

public final class DnsSpfStrings {
    public final static String DELIM = StringUtils.SPACE;
    public final static String DELIM_MOD = "=";
    public final static String DELIM_MECH = ":";

    public final static String QUAL_PASS = "+";
    public final static String QUAL_FAIL = "-";
    public final static String QUAL_FAIL_SOFT = "~";
    public final static String QUAL_NEUTRAL = "?";

    public final static String MOD_VERSION_NAME = "v";
    public final static String MOD_VERSION_VALUE_1 = "spf1";
    public final static String MOD_VERSION_1 = (MOD_VERSION_NAME + DELIM_MOD + MOD_VERSION_VALUE_1);

    public final static String MECH_ALL = "all";
    public final static String MECH_ALL_FAIL = (QUAL_FAIL + MECH_ALL);

    public final static String MECH_A = "a";

    public final static String MECH_MX = "mx";

    private DnsSpfStrings() {
    }
}
