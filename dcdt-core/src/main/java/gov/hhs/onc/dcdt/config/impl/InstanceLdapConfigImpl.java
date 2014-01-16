package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import org.xbill.DNS.Name;

public class InstanceLdapConfigImpl extends AbstractToolNamedBean implements InstanceLdapConfig {
    private Name host;
    private int port;

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Name getHost() {
        return this.host;
    }

    @Override
    public void setHost(Name host) {
        this.host = host;
    }
}
