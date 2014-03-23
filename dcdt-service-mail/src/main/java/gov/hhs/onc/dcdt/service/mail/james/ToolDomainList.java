package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.instance.InstanceDomainConfig;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.james.domainlist.api.DomainList;
import org.springframework.context.ApplicationContextAware;

public interface ToolDomainList extends ApplicationContextAware, DomainList, ToolBean {
    public InstanceDomainConfig getDefaultDomainConfig();

    public void setDefaultDomainConfig(InstanceDomainConfig defaultDomainConfig);

    public boolean hasDomainConfigs();

    @Nullable
    public List<InstanceDomainConfig> getDomainConfigs();
}
