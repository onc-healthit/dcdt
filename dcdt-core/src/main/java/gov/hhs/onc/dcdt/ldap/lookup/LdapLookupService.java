package gov.hhs.onc.dcdt.ldap.lookup;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public interface LdapLookupService extends ToolBean {
    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable Attribute ... attrs);

    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable Set<Attribute> attrs);

    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable ExprNode filter, @Nullable Attribute ... attrs);

    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable ExprNode filter, @Nullable Set<Attribute> attrs);

    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable SearchScope scope, @Nullable ExprNode filter,
        @Nullable Attribute ... attrs);

    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable SearchScope scope, @Nullable ExprNode filter,
        @Nullable Set<Attribute> attrs);

    public LdapBaseDnLookupResult lookupBaseDns(LdapConnectionConfig connConfig);
}
