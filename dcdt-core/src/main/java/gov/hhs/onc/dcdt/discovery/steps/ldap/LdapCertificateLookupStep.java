package gov.hhs.onc.dcdt.discovery.steps.ldap;

import gov.hhs.onc.dcdt.discovery.steps.CertificateLookupStep;
import gov.hhs.onc.dcdt.ldap.lookup.LdapEntryLookupResult;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import org.apache.directory.api.ldap.model.entry.Entry;

public interface LdapCertificateLookupStep
    extends CertificateLookupStep<LdapEntryLookupResult, LdapLookupService>, LdapLookupStep<Entry, LdapEntryLookupResult> {
}
