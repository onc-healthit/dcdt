package gov.hhs.onc.dcdt.utils;

import org.apache.commons.lang3.StringUtils;

public abstract class ToolUriUtils {
    public final static String DOMAIN_DELIM = ".";

    public static String joinDomains(String ... domains) {
        return ToolStringUtils.joinDelimit(domains, DOMAIN_DELIM);
    }

    public static String joinDomains(Iterable<String> domains) {
        return ToolStringUtils.joinDelimit(domains, DOMAIN_DELIM);
    }

    public static String[] splitDomains(String domain) {
        return StringUtils.split(domain, DOMAIN_DELIM);
    }
}
