package gov.hhs.onc.dcdt.beans;

import java.net.InetAddress;
import javax.annotation.Nullable;

public interface ToolDomainAddressBean extends ToolDomainBean {
    public boolean hasIpAddress();

    @Nullable
    public InetAddress getIpAddress();

    public void setIpAddress(@Nullable InetAddress ipAddr);
}
