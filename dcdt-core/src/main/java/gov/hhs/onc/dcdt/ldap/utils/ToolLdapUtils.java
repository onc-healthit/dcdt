package gov.hhs.onc.dcdt.ldap.utils;

import org.apache.directory.api.ldap.model.name.Rdn;

public abstract class ToolLdapUtils {
    public final static String PATTERN_STR_ATTR = "((?:(?:\\d|[1-9]\\d*)(?:\\.(?:\\d|[1-9]\\d*))+)|(?:[a-zA-Z][a-zA-Z0-9-]*))";

    public final static String DELIM_RDN = "=";

    public static String toString(Rdn rdn) {
        return rdn.getName() + DELIM_RDN + rdn.getValue().getString();
    }
}
