package gov.hhs.onc.dcdt.service.ldap.config.impl;

import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.server.config.beans.DirectoryServiceBean;
import org.apache.directory.server.config.beans.LdapServerBean;
import org.apache.directory.server.config.beans.TcpTransportBean;
import org.apache.directory.server.constants.ServerDNConstants;
import org.springframework.beans.factory.InitializingBean;

public class ToolDirectoryServiceBean extends DirectoryServiceBean implements InitializingBean {
    private ToolInstanceLayout instanceLayout;
    private InstanceLdapConfig ldapConfig;
    private SchemaManager schemaManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        LdapServerBean ldapServerBean = new LdapServerBean();
        ldapServerBean.setSearchBaseDn(new Dn(ServerDNConstants.USERS_SYSTEM_DN));
        ldapServerBean.setServerId(this.ldapConfig.getServerId());

        TcpTransportBean tcpTransportBean = new TcpTransportBean();
        tcpTransportBean.setSystemPort(this.ldapConfig.getBindPort());
        tcpTransportBean.setTransportAddress(this.ldapConfig.getBindAddress().getHostAddress());
        ldapServerBean.addTransports(tcpTransportBean);

        this.addServers(ldapServerBean);

        AvlPartitionBean dataPartitionBean = new AvlPartitionBean();
        dataPartitionBean.setPartitionId(this.ldapConfig.getPartitionId());
        dataPartitionBean.setPartitionSuffix(this.ldapConfig.getPartitionSuffix());
        this.addPartitions(dataPartitionBean);
    }

    public ToolInstanceLayout getInstanceLayout() {
        return this.instanceLayout;
    }

    public void setInstanceLayout(ToolInstanceLayout instanceLayout) {
        this.instanceLayout = instanceLayout;
    }

    public InstanceLdapConfig getLdapConfig() {
        return this.ldapConfig;
    }

    public void setLdapConfig(InstanceLdapConfig ldapConfig) {
        this.ldapConfig = ldapConfig;
    }

    public SchemaManager getSchemaManager() {
        return this.schemaManager;
    }

    public void setSchemaManager(SchemaManager schemaManager) {
        this.schemaManager = schemaManager;
    }
}
