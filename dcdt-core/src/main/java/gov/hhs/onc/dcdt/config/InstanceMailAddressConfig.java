package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;

public interface InstanceMailAddressConfig extends ToolNamedBean {
    public String getMailAddress();

    public void setMailAddress(String mailAddr);
}
