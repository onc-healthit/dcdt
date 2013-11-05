package gov.hhs.onc.dcdt.config;


import gov.hhs.onc.dcdt.beans.ToolBean;

public interface ToolInstance extends ToolBean {
    public String getDomain();

    public void setDomain(String domain);
}
