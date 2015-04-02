package gov.hhs.onc.dcdt.ldap.lookup.impl;

import gov.hhs.onc.dcdt.ldap.lookup.LdapBaseDnLookupResult;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.message.LdapResult;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public class LdapBaseDnLookupResultImpl extends AbstractLdapLookupResult<Dn> implements LdapBaseDnLookupResult {
    public LdapBaseDnLookupResultImpl(LdapConnectionConfig connConfig, LdapResult result) {
        this(connConfig, result, null);
    }

    public LdapBaseDnLookupResultImpl(LdapConnectionConfig connConfig, LdapResult result, @Nullable List<Dn> items) {
        super(connConfig, result, items);
    }
}
