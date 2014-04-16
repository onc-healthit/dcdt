package gov.hhs.onc.dcdt.ldap.lookup;

import java.util.Set;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;

public interface LdapEntryLookupResult extends LdapLookupResult<Entry> {
    public boolean hasAttributes();

    @Nullable
    public Set<Attribute> getAttributes();

    public Dn getBaseDn();

    public ExprNode getFilter();

    public SearchScope getScope();
}
