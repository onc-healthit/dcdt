package gov.hhs.onc.dcdt.testcases.discovery.credentials.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.config.InstanceDomainConfig;
import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import gov.hhs.onc.dcdt.testcases.LocationType;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialLocation;
import javax.annotation.Nullable;

@JsonTypeName("discoveryTestcaseCredLoc")
public class DiscoveryTestcaseCredentialLocationImpl extends AbstractToolBean implements DiscoveryTestcaseCredentialLocation {
    private InstanceDomainConfig instanceDomainConfig;
    private InstanceLdapConfig instanceLdapConfig;
    private LocationType locType;

    @Override
    public InstanceDomainConfig getInstanceDomainConfig() {
        return this.instanceDomainConfig;
    }

    @Override
    public void setInstanceDomainConfig(InstanceDomainConfig instanceDomainConfig) {
        this.instanceDomainConfig = instanceDomainConfig;
    }

    @Override
    public boolean hasInstanceLdapConfig() {
        return this.instanceLdapConfig != null;
    }

    @Nullable
    @Override
    public InstanceLdapConfig getInstanceLdapConfig() {
        return this.instanceLdapConfig;
    }

    @Override
    public void setInstanceLdapConfig(@Nullable InstanceLdapConfig instanceLdapConfig) {
        this.instanceLdapConfig = instanceLdapConfig;
    }

    @Override
    public LocationType getType() {
        return this.locType;
    }

    @Override
    public void setType(LocationType locType) {
        this.locType = locType;
    }
}
