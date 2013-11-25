package gov.hhs.onc.dcdt.config.impl;


import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.utils.ToolFileUtils;
import java.io.File;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@JsonTypeName("instanceConfig")
public class InstanceConfigImpl extends AbstractToolBean implements InstanceConfig {
    private transient File dir;
    private String domain;

    @Override
    public boolean hasDirectory() {
        return ToolFileUtils.isDirectory(dir);
    }

    @Override
    public File getDirectory() {
        return this.dir;
    }

    @Override
    public void setDirectory(File dir) {
        this.dir = dir;
    }

    @Override
    public boolean hasDirectoryPath() {
        return this.hasDirectory();
    }

    @Override
    public String getDirectoryPath() {
        return ObjectUtils.toString(this.dir, null);
    }

    @Override
    public void setDirectoryPath(String dirPath) {
        this.dir = new File(dirPath);
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
