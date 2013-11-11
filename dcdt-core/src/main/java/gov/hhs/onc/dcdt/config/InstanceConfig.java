package gov.hhs.onc.dcdt.config;


import gov.hhs.onc.dcdt.beans.ToolBean;
import java.io.File;

public interface InstanceConfig extends ToolBean {
    public boolean hasDirectory();

    public File getDirectory();

    public void setDirectory(File dir);

    public boolean hasDomain();

    public String getDomain();

    public void setDomain(String domain);
}
