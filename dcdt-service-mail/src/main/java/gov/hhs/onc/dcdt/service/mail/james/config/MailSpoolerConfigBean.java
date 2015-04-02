package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import javax.annotation.Nullable;

@ConfigurationNode(name = "spooler")
public interface MailSpoolerConfigBean extends JamesConfigBean {
    public boolean hasDequeueThreads();

    @ConfigurationNode
    @Nullable
    public Integer getDequeueThreads();

    public void setDequeueThreads(@Nullable Integer dequeueThreads);

    public boolean hasThreads();

    @ConfigurationNode
    @Nullable
    public Integer getThreads();

    public void setThreads(@Nullable Integer threads);
}
