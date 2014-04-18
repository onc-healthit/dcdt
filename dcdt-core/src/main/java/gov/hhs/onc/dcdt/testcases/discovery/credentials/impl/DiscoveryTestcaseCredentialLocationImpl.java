package gov.hhs.onc.dcdt.testcases.discovery.credentials.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.config.instance.InstanceLdapConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.discovery.LocationType;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialLocation;
import javax.annotation.Nullable;

@JsonTypeName("discoveryTestcaseCredLoc")
public class DiscoveryTestcaseCredentialLocationImpl extends AbstractToolBean implements DiscoveryTestcaseCredentialLocation {
    private InstanceLdapConfig ldapConfig;
    private MailAddress mailAddr;
    private LocationType locType;

    @Override
    public boolean hasLdapConfig() {
        return this.ldapConfig != null;
    }

    @Nullable
    @Override
    public InstanceLdapConfig getLdapConfig() {
        return this.ldapConfig;
    }

    @Override
    public void setLdapConfig(@Nullable InstanceLdapConfig ldapConfig) {
        this.ldapConfig = ldapConfig;
    }

    @Override
    public boolean hasMailAddress() {
        return this.mailAddr != null;
    }

    @Nullable
    @Override
    public MailAddress getMailAddress() {
        return this.mailAddr;
    }

    @Override
    public void setMailAddress(@Nullable MailAddress mailAddr) {
        this.mailAddr = mailAddr;
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
