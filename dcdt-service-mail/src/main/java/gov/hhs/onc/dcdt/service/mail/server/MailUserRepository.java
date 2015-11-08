package gov.hhs.onc.dcdt.service.mail.server;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import javax.annotation.Nullable;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.context.ApplicationContextAware;

public interface MailUserRepository extends ApplicationContextAware, ToolBean {
    @Nullable
    public InstanceMailAddressConfig findAuthenticatedConfig(String id, String secret);

    public boolean hasAuthenticatedConfigs();

    @Nullable
    public MultiKeyMap<String, InstanceMailAddressConfig> getAuthenticatedConfigs();
}
