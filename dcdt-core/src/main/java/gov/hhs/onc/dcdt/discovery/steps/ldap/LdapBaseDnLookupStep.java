package gov.hhs.onc.dcdt.discovery.steps.ldap;

import gov.hhs.onc.dcdt.ldap.lookup.LdapBaseDnLookupResult;
import org.apache.directory.api.ldap.model.name.Dn;

public interface LdapBaseDnLookupStep extends LdapLookupStep<Dn, LdapBaseDnLookupResult> {
}
