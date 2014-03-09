package gov.hhs.onc.dcdt.service.ldap.config.impl;

import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import org.apache.directory.server.config.beans.LdapServerBean;
import org.apache.directory.server.config.beans.TcpTransportBean;
import org.springframework.beans.factory.InitializingBean;

public class ToolLdapServerBean extends LdapServerBean implements InitializingBean {
    private InstanceLdapConfig ldapConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setServerId(this.ldapConfig.getServerId());

        TcpTransportBean tcpTransportBean = new TcpTransportBean();
        tcpTransportBean.setSystemPort(this.ldapConfig.getBindPort());
        tcpTransportBean.setTransportAddress(this.ldapConfig.getBindAddress().getHostAddress());
        this.addTransports(tcpTransportBean);
    }

    public InstanceLdapConfig getLdapConfig() {
        return this.ldapConfig;
    }

    public void setLdapConfig(InstanceLdapConfig ldapConfig) {
        this.ldapConfig = ldapConfig;
    }
}
