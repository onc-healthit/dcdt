package gov.hhs.onc.dcdt.ldap.lookup.impl;

import gov.hhs.onc.dcdt.ldap.lookup.LdapEntryLookupResult;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.message.LdapResult;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public class LdapEntryLookupResultImpl extends AbstractLdapLookupResult<Entry> implements LdapEntryLookupResult {
    private Set<Attribute> attrs;
    private Dn baseDn;
    private ExprNode filter;
    private SearchScope scope;

    public LdapEntryLookupResultImpl(LdapConnectionConfig connConfig, Dn baseDn, SearchScope scope, ExprNode filter, @Nullable Set<Attribute> attrs,
        LdapResult result) {
        this(connConfig, baseDn, scope, filter, attrs, result, null);
    }

    public LdapEntryLookupResultImpl(LdapConnectionConfig connConfig, Dn baseDn, SearchScope scope, ExprNode filter, @Nullable Set<Attribute> attrs,
        LdapResult result, @Nullable List<Entry> items) {
        super(connConfig, result, items);

        this.baseDn = baseDn;
        this.scope = scope;
        this.filter = filter;
        this.attrs = attrs;
    }

    @Override
    public boolean hasAttributes() {
        return !CollectionUtils.isEmpty(this.attrs);
    }

    @Nullable
    @Override
    public Set<Attribute> getAttributes() {
        return this.attrs;
    }

    @Override
    public Dn getBaseDn() {
        return this.baseDn;
    }

    @Override
    public ExprNode getFilter() {
        return this.filter;
    }

    @Override
    public SearchScope getScope() {
        return this.scope;
    }
}
