package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.net.InetAddress;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;

public interface InstanceConfig extends ToolBean {
    public boolean hasDomainName();

    @Nullable
    public Name getDomainName();

    public void setDomainName(@Nullable Name domainName);

    public boolean hasIpAddress();

    @Nullable
    public InetAddress getIpAddress();

    public void setIpAddress(@Nullable InetAddress ipAddr);
}
