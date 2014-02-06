package gov.hhs.onc.dcdt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.impl.InstanceConfigImpl;
import java.net.InetAddress;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;

@JsonSubTypes({ @Type(InstanceConfigImpl.class) })
public interface InstanceConfig extends ToolBean {
    public boolean hasDomainName();

    @JsonProperty("domainName")
    @Nullable
    public Name getDomainName();

    public void setDomainName(@Nullable Name domainName);

    public boolean hasIpAddress();

    @JsonProperty("ipAddr")
    @Nullable
    public InetAddress getIpAddress();

    public void setIpAddress(@Nullable InetAddress ipAddr);
}
