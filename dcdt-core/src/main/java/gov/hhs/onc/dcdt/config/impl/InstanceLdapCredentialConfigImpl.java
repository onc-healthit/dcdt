package gov.hhs.onc.dcdt.config.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolConnectionCredentialBean;
import gov.hhs.onc.dcdt.config.InstanceLdapCredentialConfig;
import org.apache.directory.api.ldap.model.name.Dn;

@JsonTypeName("instanceLdapCredConfig")
public class InstanceLdapCredentialConfigImpl extends AbstractToolConnectionCredentialBean<Dn, String> implements InstanceLdapCredentialConfig {
}
