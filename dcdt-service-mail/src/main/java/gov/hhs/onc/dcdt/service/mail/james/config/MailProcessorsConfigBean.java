package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import java.util.List;
import javax.annotation.Nullable;

@ConfigurationNode(name = "processors")
public interface MailProcessorsConfigBean extends JamesConfigBean {
    public boolean hasProcessors();

    @ConfigurationNode(name = "processor")
    @Nullable
    public List<MailProcessorConfigBean> getProcessors();

    public void setProcessors(@Nullable List<MailProcessorConfigBean> procs);
}
