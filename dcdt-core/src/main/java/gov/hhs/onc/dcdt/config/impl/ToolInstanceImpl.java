package gov.hhs.onc.dcdt.config.impl;


import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.config.ToolInstance;

public class ToolInstanceImpl extends AbstractToolBean implements ToolInstance {
    private String domain;

    @Override
    public String getDomain() {
        return this.domain;
    }

    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }
}
