package gov.hhs.onc.dcdt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.impl.InstanceConfigImpl;
import gov.hhs.onc.dcdt.validation.constraints.Domain;
import gov.hhs.onc.dcdt.validation.constraints.io.FileExists;
import java.io.File;

@JsonRootName("instanceConfig")
@JsonSubTypes({ @Type(InstanceConfigImpl.class) })
public interface InstanceConfig extends ToolBean {
    public boolean hasDirectory();

    @FileExists(false)
    @JsonProperty("directory")
    public File getDirectory();

    public void setDirectory(File dir);

    public boolean hasDirectoryPath();

    public String getDirectoryPath();

    public void setDirectoryPath(String dirPath);

    public boolean hasDomain();

    @Domain
    @JsonProperty("domain")
    public String getDomain();

    public void setDomain(String domain);
}
