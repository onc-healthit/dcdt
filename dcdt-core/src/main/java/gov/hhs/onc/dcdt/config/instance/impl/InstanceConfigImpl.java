package gov.hhs.onc.dcdt.config.instance.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolDomainBean;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import java.net.InetAddress;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.xbill.DNS.Name;

@Entity(name = "instance_config")
@Table(name = "instance_configs")
public class InstanceConfigImpl extends AbstractToolDomainBean implements InstanceConfig {
    private InetAddress ipAddr;

    @Override
    @Transient
    public boolean isConfigured() {
        return (this.hasIpAddress() && this.hasDomainName());
    }

    @Column(name = "domain_name", nullable = false)
    @Id
    @Nullable
    @Override
    public Name getDomainName() {
        return super.getDomainName();
    }

    @Override
    public boolean hasIpAddress() {
        return this.ipAddr != null;
    }

    @Column(name = "ip_address", nullable = false)
    @Nullable
    @Override
    public InetAddress getIpAddress() {
        return this.ipAddr;
    }

    @Override
    public void setIpAddress(@Nullable InetAddress ipAddr) {
        this.ipAddr = ipAddr;
    }
}
