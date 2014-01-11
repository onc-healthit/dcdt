package gov.hhs.onc.dcdt.config.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.io.utils.ToolFileUtils;
import java.io.File;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

@Entity(name = "instance_config")
@JsonTypeName("instanceConfig")
@NamedNativeQueries({ @NamedNativeQuery(name = AbstractToolBean.QUERY_NAME_GET_ALL_BEANS, query = "select * from instance_configs", resultClass = InstanceConfigImpl.class) })
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

    @Transient
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

    @Column(name = "domain", nullable = false)
    @Id
    @Nullable
    @Override
    public String getDomain() {
        return this.domain;
    }

    @Override
    public void setDomain(@Nullable String domain) {
        this.domain = domain;
    }
}
