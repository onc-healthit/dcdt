package gov.hhs.onc.dcdt.discovery.steps.ldap;

import gov.hhs.onc.dcdt.discovery.steps.LookupStep;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupResult;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import java.io.Externalizable;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;

public interface LdapLookupStep<T extends Externalizable, U extends LdapLookupResult<T>> extends LookupStep<T, ResultCodeEnum, U, LdapLookupService> {
}
