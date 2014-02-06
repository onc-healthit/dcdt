package gov.hhs.onc.dcdt.config.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import java.net.InetAddress;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import org.xbill.DNS.Name;

@Entity(name = "instance_config")
@JsonTypeName("instanceConfig")
@Table(name = "instance_configs")
public class InstanceConfigImpl extends AbstractToolBean implements InstanceConfig {
    private Name domainName;
    private InetAddress ipAddr;

    @Override
    public boolean hasDomainName() {
        return this.domainName != null;
    }

    @Column(name = "domain_name", nullable = false)
    @Id
    @Nullable
    @Override
    @Valid
    public Name getDomainName() {
        return this.domainName;
    }

    @Override
    public void setDomainName(@Nullable Name domainName) {
        this.domainName = domainName;
    }

    @Override
    public boolean hasIpAddress() {
        return this.ipAddr != null;
    }

    @Column(name = "ip_address", nullable = false)
    @Nullable
    @Override
    @Valid
    public InetAddress getIpAddress() {
        return this.ipAddr;
    }

    @Override
    public void setIpAddress(@Nullable InetAddress ipAddr) {
        this.ipAddr = ipAddr;
    }
}
