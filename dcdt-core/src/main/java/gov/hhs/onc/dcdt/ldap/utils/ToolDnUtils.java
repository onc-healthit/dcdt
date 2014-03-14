package gov.hhs.onc.dcdt.ldap.utils;

import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.Collection;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.name.Rdn;

public abstract class ToolDnUtils {
    public final static String DELIM_RDNS = ",";

    public static Dn fromRdns(Collection<Rdn> rdns) throws ToolLdapException {
        return fromRdns(ToolCollectionUtils.toArray(rdns, Rdn.class));
    }

    public static Dn fromRdns(Rdn ... rdns) throws ToolLdapException {
        try {
            return new Dn(rdns);
        } catch (LdapInvalidDnException e) {
            throw new ToolLdapException(String.format("Unable to read DN from RDN(s): [%s]", ToolStringUtils.joinDelimit(rdns, ", ")), e);
        }
    }

    public static Dn fromStrings(Collection<String> rdnStrs) throws ToolLdapException {
        return fromStrings(ToolCollectionUtils.toArray(rdnStrs, String.class));
    }

    public static Dn fromStrings(String ... rdnStrs) throws ToolLdapException {
        try {
            return new Dn(rdnStrs);
        } catch (LdapInvalidDnException e) {
            throw new ToolLdapException(String.format("Unable to read DN from RDN string(s): [%s]", ToolStringUtils.joinDelimit(rdnStrs, ", ")), e);
        }
    }
}
