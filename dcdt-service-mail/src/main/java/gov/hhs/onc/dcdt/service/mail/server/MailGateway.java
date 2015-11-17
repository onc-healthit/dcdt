package gov.hhs.onc.dcdt.service.mail.server;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.Map;
import javax.annotation.Nullable;
import org.springframework.context.ApplicationContextAware;

public interface MailGateway extends ApplicationContextAware, ToolBean {
    @Nullable
    public InstanceMailAddressConfig authenticate(String id, String secret);

    public boolean hasAddressConfigs();

    @Nullable
    public Map<MailAddress, InstanceMailAddressConfig> getAddressConfigs();
}
