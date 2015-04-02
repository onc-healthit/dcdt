package gov.hhs.onc.dcdt.discovery.steps.ldap;

import gov.hhs.onc.dcdt.discovery.steps.CertificateLookupStep;
import gov.hhs.onc.dcdt.ldap.lookup.LdapEntryLookupResult;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;

public interface LdapCertificateLookupStep extends CertificateLookupStep<Entry, ResultCodeEnum, LdapEntryLookupResult, LdapLookupService>,
    LdapLookupStep<Entry, LdapEntryLookupResult> {
}
