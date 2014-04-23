package gov.hhs.onc.dcdt.service.ldap;

import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.ldap.config.impl.ToolDirectoryServiceBean;
import java.util.List;
import javax.annotation.Nullable;

public interface LdapService extends ToolService {
    public boolean hasDirectoryServiceBeans();

    @Nullable
    public List<ToolDirectoryServiceBean> getDirectoryServiceBeans();
}
