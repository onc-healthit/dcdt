package gov.hhs.onc.dcdt.ldap.utils;

import org.apache.directory.api.ldap.model.name.Rdn;

public abstract class ToolLdapUtils {
    public final static String DELIM_RDN = "=";

    public static String toString(Rdn rdn) {
        return rdn.getName() + DELIM_RDN + rdn.getValue().getString();
    }
}
