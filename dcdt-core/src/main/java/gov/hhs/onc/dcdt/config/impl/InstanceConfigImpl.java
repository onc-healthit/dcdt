package gov.hhs.onc.dcdt.config.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.io.utils.ToolFileUtils;
import java.io.File;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("instanceConfig")
@Entity(name = "instance_config")
@JsonTypeName("instanceConfig")
@Scope("singleton")
@Table(name = "instance_configs")
public class InstanceConfigImpl extends AbstractToolBean implements InstanceConfig {
    @Transient
    @Value("${dcdt.instance.dir}")
    private File dir;

    @Transient
    @Value("${dcdt.instance.db.dir}")
    private File dbDir;

    @Transient
    @Value("${dcdt.instance.db.user}")
    private String dbUser;

    @Transient
    @Value("${dcdt.instance.db.pass}")
    private String dbPass;

    @Column(name = "domain", nullable = false)
    @Id
    private String domain;

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
    public boolean hasDomain() {
        return !StringUtils.isBlank(this.domain);
    }

    @Override
    public String getDomain() {
        return this.domain;
    }

    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }
}
