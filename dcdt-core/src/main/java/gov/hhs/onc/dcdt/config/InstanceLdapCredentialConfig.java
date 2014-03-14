package gov.hhs.onc.dcdt.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolConnectionCredentialBean;
import gov.hhs.onc.dcdt.config.impl.InstanceLdapCredentialConfigImpl;
import org.apache.directory.api.ldap.model.name.Dn;

@JsonSubTypes({ @Type(InstanceLdapCredentialConfigImpl.class) })
public interface InstanceLdapCredentialConfig extends ToolConnectionCredentialBean<Dn, String> {
}
