package gov.hhs.onc.dcdt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.impl.InstanceConfigImpl;
import gov.hhs.onc.dcdt.validation.constraints.Domain;
import java.io.File;
import javax.annotation.Nullable;

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

    public boolean hasDomain();

    @Domain
    @JsonProperty("domain")
    @Nullable
    public String getDomain();

    public void setDomain(@Nullable String domain);
}
