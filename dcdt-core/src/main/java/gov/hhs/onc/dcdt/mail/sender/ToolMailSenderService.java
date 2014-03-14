package gov.hhs.onc.dcdt.mail.sender;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.InstanceMailAddressConfig;
import java.util.List;
import javax.annotation.Nullable;
import org.springframework.context.ApplicationContextAware;

public interface ToolMailSenderService extends ApplicationContextAware, ToolBean {
    public InstanceMailAddressConfig getFromConfig();

    public boolean hasTransportListenerBeanNames();

    @Nullable
    public List<String> getTransportListenerBeanNames();

    public void setTransportListenerBeanNames(@Nullable List<String> transportListenerBeanNames);
}
