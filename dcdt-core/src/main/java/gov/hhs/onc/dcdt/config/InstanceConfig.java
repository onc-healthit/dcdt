package gov.hhs.onc.dcdt.config;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.impl.InstanceConfigImpl;
import java.io.File;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

@JsonRootName("instanceConfig")
@JsonSubTypes({ @Type(InstanceConfigImpl.class) })
public interface InstanceConfig extends ToolBean {
    public boolean hasDirectory();

    @JsonProperty(value = "directory")
    @NotNull
    public File getDirectory();

    public void setDirectory(File dir);

    public boolean hasDirectoryPath();

    public String getDirectoryPath();

    public void setDirectoryPath(String dirPath);

    public boolean hasDomain();

    @JsonProperty(value = "domain")
    @URL(regexp = "^[\\w\\-\\.]+$")
    public String getDomain();

    public void setDomain(String domain);
}
