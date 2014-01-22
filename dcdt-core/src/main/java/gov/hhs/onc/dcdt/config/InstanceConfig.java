package gov.hhs.onc.dcdt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.impl.InstanceConfigImpl;
import gov.hhs.onc.dcdt.dns.DnsNameException;
import gov.hhs.onc.dcdt.net.Domain;
import gov.hhs.onc.dcdt.net.IpAddress;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;

@JsonSubTypes({ @Type(InstanceConfigImpl.class) })
public interface InstanceConfig extends ToolBean {
    public boolean hasDatabaseDirectory();

    public File getDatabaseDirectory();

    public void setDatabaseDirectory(File dbDir);

    public boolean hasDatabasePassword();

    public String getDatabasePassword();

    public void setDatabasePassword(String dbPass);

    public boolean hasDatabaseUser();

    public String getDatabaseUser();

    public void setDatabaseUser(String dbUser);

    public boolean hasDirectory();

    public File getDirectory();

    public void setDirectory(File dir);

    public boolean hasDomainName();

    @Nullable
    public Name getDomainName();

    public void setDomainName(@Nullable Name domainName);

    public boolean hasDomainNameString();

    @Domain
    @JsonProperty("domainName")
    @Nullable
    public String getDomainNameString();

    public void setDomainNameString(@Nullable String domainNameStr) throws DnsNameException;

    public boolean hasIpAddress();

    @Nullable
    public InetAddress getIpAddress();

    public void setIpAddress(@Nullable InetAddress ipAddr);

    public boolean hasIpAddressString();

    @IpAddress
    @JsonProperty("ipAddr")
    @Nullable
    public String getIpAddressString();

    public void setIpAddressString(@Nullable String ipAddrStr) throws UnknownHostException;
}
