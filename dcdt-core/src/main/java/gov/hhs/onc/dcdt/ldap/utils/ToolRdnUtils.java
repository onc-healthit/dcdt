package gov.hhs.onc.dcdt.ldap.utils;

import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Ava;
import org.apache.directory.api.ldap.model.name.Rdn;

public abstract class ToolRdnUtils {
    public final static String DELIM_RDN = "=";

    public static Rdn fromAva(Ava ava) throws ToolLdapException {
        try {
            return new Rdn(ava.getType(), ava.getValue().getString());
        } catch (LdapInvalidDnException e) {
            throw new ToolLdapException(String.format("Unable to read RDN from ava: {%s}", ava), e);
        }
    }

    public static Rdn fromString(String str) throws ToolLdapException {
        try {
            return new Rdn(str);
        } catch (LdapInvalidDnException e) {
            throw new ToolLdapException(String.format("Unable to read RDN from string: %s", str), e);
        }
    }
}
