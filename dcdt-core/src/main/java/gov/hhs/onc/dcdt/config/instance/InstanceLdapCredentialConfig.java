package gov.hhs.onc.dcdt.config.instance;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolConnectionPasswordCredentialBean;
import gov.hhs.onc.dcdt.config.instance.impl.InstanceLdapCredentialConfigImpl;
import org.apache.directory.api.ldap.model.name.Dn;

@JsonSubTypes({ @Type(InstanceLdapCredentialConfigImpl.class) })
public interface InstanceLdapCredentialConfig extends ToolConnectionPasswordCredentialBean<Dn> {
}
