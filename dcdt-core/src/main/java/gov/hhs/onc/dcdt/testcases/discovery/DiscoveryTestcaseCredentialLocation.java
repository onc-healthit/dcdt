package gov.hhs.onc.dcdt.testcases.discovery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.InstanceDomainConfig;
import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import gov.hhs.onc.dcdt.testcases.LocationType;
import gov.hhs.onc.dcdt.testcases.discovery.impl.DiscoveryTestcaseCredentialLocationImpl;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(DiscoveryTestcaseCredentialLocationImpl.class) })
public interface DiscoveryTestcaseCredentialLocation extends ToolBean {
    @JsonProperty("instanceDomainConfig")
    public InstanceDomainConfig getInstanceDomainConfig();

    public void setInstanceDomainConfig(InstanceDomainConfig instanceDomainConfig);

    public boolean hasInstanceLdapConfig();

    @JsonProperty("instanceLdapConfig")
    @Nullable
    public InstanceLdapConfig getInstanceLdapConfig();

    public void setInstanceLdapConfig(@Nullable InstanceLdapConfig instanceLdapConfig);

    @JsonProperty("type")
    public LocationType getType();

    public void setType(LocationType locType);
}
