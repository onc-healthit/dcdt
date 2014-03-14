package gov.hhs.onc.dcdt.testcases.discovery.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.LocationType;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.impl.DiscoveryTestcaseCredentialLocationImpl;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(DiscoveryTestcaseCredentialLocationImpl.class) })
public interface DiscoveryTestcaseCredentialLocation extends ToolBean {
    public boolean hasLdapConfig();

    @JsonProperty("ldapConfig")
    @Nullable
    public InstanceLdapConfig getLdapConfig();

    public void setLdapConfig(@Nullable InstanceLdapConfig ldapConfig);

    public boolean hasMailAddress();

    @JsonProperty("mailAddr")
    @Nullable
    public MailAddress getMailAddress();

    public void setMailAddress(@Nullable MailAddress mailAddr);

    @JsonProperty("type")
    public LocationType getType();

    public void setType(LocationType locType);
}
