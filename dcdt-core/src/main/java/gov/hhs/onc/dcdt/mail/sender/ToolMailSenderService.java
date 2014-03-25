package gov.hhs.onc.dcdt.mail.sender;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import javax.annotation.Nullable;
import org.springframework.context.ApplicationContextAware;

public interface ToolMailSenderService extends ApplicationContextAware, ToolBean {
    public InstanceMailAddressConfig getFromConfig();

    public boolean hasReplyToConfig();

    @Nullable
    public InstanceMailAddressConfig getReplyToConfig();
}
