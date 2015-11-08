package gov.hhs.onc.dcdt.service.ldap.server;

import gov.hhs.onc.dcdt.service.ldap.config.LdapServerConfig;
import gov.hhs.onc.dcdt.service.ldap.config.impl.ToolDirectoryServiceBean;
import gov.hhs.onc.dcdt.service.server.ToolServer;
import java.util.List;
import javax.annotation.Nullable;

public interface LdapServer extends ToolServer<LdapServerConfig> {
    public boolean hasDirectoryServiceBeans();

    @Nullable
    public List<ToolDirectoryServiceBean> getDirectoryServiceBeans();

    public void setDirectoryServiceBeans(List<ToolDirectoryServiceBean> dirServiceBeans);
}
