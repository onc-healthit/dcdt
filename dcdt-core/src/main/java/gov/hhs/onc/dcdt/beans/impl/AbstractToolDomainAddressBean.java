package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolDomainAddressBean;
import java.net.InetAddress;
import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractToolDomainAddressBean extends AbstractToolDomainBean implements ToolDomainAddressBean {
    protected InetAddress ipAddr;

    @Override
    public boolean hasIpAddress() {
        return this.ipAddr != null;
    }

    @Nullable
    @Override
    @Transient
    public InetAddress getIpAddress() {
        return this.ipAddr;
    }

    @Override
    public void setIpAddress(@Nullable InetAddress ipAddr) {
        this.ipAddr = ipAddr;
    }
}
