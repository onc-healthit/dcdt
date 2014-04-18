package gov.hhs.onc.dcdt.discovery.steps.ldap.impl;

import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.LocationType;
import gov.hhs.onc.dcdt.discovery.steps.impl.AbstractLookupStep;
import gov.hhs.onc.dcdt.discovery.steps.ldap.LdapLookupStep;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupResult;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import java.io.Externalizable;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;

public abstract class AbstractLdapLookupStep<T extends Externalizable, U extends LdapLookupResult<T>> extends
    AbstractLookupStep<T, ResultCodeEnum, U, LdapLookupService> implements LdapLookupStep<T, U> {
    protected AbstractLdapLookupStep(BindingType bindingType, LdapLookupService lookupService) {
        super(bindingType, LocationType.LDAP, lookupService);
    }
}
