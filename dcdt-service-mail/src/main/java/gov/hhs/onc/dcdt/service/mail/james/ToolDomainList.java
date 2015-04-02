package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.instance.InstanceDomainConfig;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.james.domainlist.api.DomainList;
import org.springframework.context.ApplicationContextAware;
import org.xbill.DNS.Name;

public interface ToolDomainList extends ApplicationContextAware, DomainList, ToolBean {
    public boolean hasDefaultDomainName();

    @Nullable
    public Name getDefaultDomainName();

    public void setDefaultDomainName(@Nullable Name defaultDomainName);

    public Name getDefaultDomainNameFallback();

    public void setDefaultDomainNameFallback(Name defaultDomainNameFallback);

    public boolean hasDomainConfigs();

    @Nullable
    public List<InstanceDomainConfig> getDomainConfigs();
}
