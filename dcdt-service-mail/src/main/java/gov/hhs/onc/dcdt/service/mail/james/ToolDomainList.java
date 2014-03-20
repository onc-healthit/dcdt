package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.instance.InstanceDomainConfig;
import java.util.List;
import org.apache.james.domainlist.api.DomainList;

public interface ToolDomainList extends DomainList, ToolBean {
    public InstanceDomainConfig getDefaultDomainConfig();

    public void setDefaultDomainConfig(InstanceDomainConfig defaultDomainConfig);

    public List<InstanceDomainConfig> getDomainConfigs();

    public void setDomainConfigs(List<InstanceDomainConfig> domainConfigs);
}
