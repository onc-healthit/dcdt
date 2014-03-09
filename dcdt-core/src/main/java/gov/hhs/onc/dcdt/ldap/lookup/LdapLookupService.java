package gov.hhs.onc.dcdt.ldap.lookup;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public interface LdapLookupService extends ToolBean {
    public List<Entry> search(LdapConnectionConfig ldapConnConfig, Attribute ... searchAttrs) throws ToolLdapException;

    public List<Entry> search(LdapConnectionConfig ldapConnConfig, Iterable<Attribute> searchAttrs) throws ToolLdapException;

    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable ExprNode searchFilter, Attribute ... searchAttrs) throws ToolLdapException;

    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable ExprNode searchFilter, Iterable<Attribute> searchAttrs) throws ToolLdapException;

    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable SearchScope searchScope, @Nullable ExprNode searchFilter,
        Attribute ... searchAttrs) throws ToolLdapException;

    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable SearchScope searchScope, @Nullable ExprNode searchFilter,
        Iterable<Attribute> searchAttrs) throws ToolLdapException;

    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable Dn baseDn, @Nullable SearchScope searchScope, @Nullable ExprNode searchFilter,
        Attribute ... searchAttrs) throws ToolLdapException;

    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable Dn baseDn, @Nullable SearchScope searchScope, @Nullable ExprNode searchFilter,
        Iterable<Attribute> searchAttrs) throws ToolLdapException;

    public List<Dn> getBaseDns(LdapConnectionConfig ldapConnConfig) throws ToolLdapException;
}
