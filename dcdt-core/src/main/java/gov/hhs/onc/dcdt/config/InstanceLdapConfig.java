package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import org.xbill.DNS.Name;

public interface InstanceLdapConfig extends ToolNamedBean {
    public Name getHost();

    public void setHost(Name host);

    public int getPort();

    public void setPort(int port);
}
