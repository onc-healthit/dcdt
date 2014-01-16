package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.InstanceDomainConfig;
import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import gov.hhs.onc.dcdt.testcases.LocationType;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseCredentialLocation extends ToolBean {
    public InstanceDomainConfig getInstanceDomainConfig();

    public void setInstanceDomainConfig(InstanceDomainConfig instanceDomainConfig);

    public boolean hasInstanceLdapConfig();

    @Nullable
    public InstanceLdapConfig getInstanceLdapConfig();

    public void setInstanceLdapConfig(@Nullable InstanceLdapConfig instanceLdapConfig);

    public LocationType getType();

    public void setType(LocationType locType);
}
