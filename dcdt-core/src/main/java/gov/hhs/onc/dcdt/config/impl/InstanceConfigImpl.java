package gov.hhs.onc.dcdt.config.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.dns.DnsNameException;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.io.utils.ToolFileUtils;
import gov.hhs.onc.dcdt.net.utils.ToolInetAddressUtils;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.xbill.DNS.Name;

@Entity(name = "instance_config")
@JsonTypeName("instanceConfig")
@Table(name = "instance_configs")
public class InstanceConfigImpl extends AbstractToolBean implements InstanceConfig {
    @Value("${dcdt.instance.dir}")
    private File dir;

    @Value("${dcdt.instance.db.dir}")
    private File dbDir;

    @Value("${dcdt.instance.db.user}")
    private String dbUser;

    @Value("${dcdt.instance.db.pass}")
    private String dbPass;

    private Name domainName;
    private InetAddress ipAddr;

    @Override
    public boolean hasDatabaseDirectory() {
        return ToolFileUtils.isDirectory(ToolFileUtils.toPath(this.dir));
    }

    @Override
    @Transient
    public File getDatabaseDirectory() {
        return this.dbDir;
    }

    @Override
    public void setDatabaseDirectory(File dbDir) {
        this.dbDir = dbDir;
    }

    @Override
    public boolean hasDatabasePassword() {
        return !StringUtils.isBlank(this.dbPass);
    }

    @Override
    @Transient
    public String getDatabasePassword() {
        return this.dbPass;
    }

    @Override
    public void setDatabasePassword(String dbPass) {
        this.dbPass = dbPass;
    }

    @Override
    public boolean hasDatabaseUser() {
        return !StringUtils.isBlank(this.dbUser);
    }

    @Override
    @Transient
    public String getDatabaseUser() {
        return this.dbUser;
    }

    @Override
    public void setDatabaseUser(String dbUser) {
        this.dbUser = dbUser;
    }

    @Override
    public boolean hasDirectory() {
        return ToolFileUtils.isDirectory(ToolFileUtils.toPath(this.dir));
    }

    @Override
    @Transient
    public File getDirectory() {
        return this.dir;
    }

    @Override
    public void setDirectory(File dir) {
        this.dir = dir;
    }

    @Override
    public boolean hasDomainName() {
        return this.domainName != null;
    }

    @Column(name = "domain_name", nullable = false)
    @Id
    @Nullable
    @Override
    public Name getDomainName() {
        return this.domainName;
    }

    @Override
    public void setDomainName(@Nullable Name domainName) {
        this.domainName = domainName;
    }

    @Override
    public boolean hasDomainNameString() {
        return !StringUtils.isBlank(this.getDomainNameString());
    }

    @Nullable
    @Override
    @Transient
    public String getDomainNameString() {
        return this.hasDomainName() ? this.domainName.toString() : null;
    }

    @Override
    public void setDomainNameString(@Nullable String domainNameStr) throws DnsNameException {
        this.domainName = ToolDnsNameUtils.fromLabelStrings(domainNameStr);
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

    @Override
    public boolean hasIpAddressString() {
        return !StringUtils.isBlank(this.getIpAddressString());
    }

    @Nullable
    @Override
    @Transient
    public String getIpAddressString() {
        return this.hasIpAddress() ? this.ipAddr.getHostAddress() : null;
    }

    @Override
    public void setIpAddressString(@Nullable String ipAddrStr) throws UnknownHostException {
        this.ipAddr = ToolInetAddressUtils.getByAddress(null, ipAddrStr);
    }
}
