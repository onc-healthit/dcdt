package gov.hhs.onc.dcdt.config.instance.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolConnectionPasswordCredentialBean;
import gov.hhs.onc.dcdt.config.instance.InstanceLdapCredentialConfig;
import org.apache.directory.api.ldap.model.name.Dn;

@JsonTypeName("instanceLdapCredConfig")
public class InstanceLdapCredentialConfigImpl extends AbstractToolConnectionPasswordCredentialBean<Dn> implements InstanceLdapCredentialConfig {
}
