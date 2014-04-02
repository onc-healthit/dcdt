package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import java.util.List;
import javax.annotation.Nullable;

@ConfigurationNode(name = "smtpservers")
public interface SmtpServersConfigBean extends JamesConfigBean {
    public boolean hasServers();

    @ConfigurationNode(name = "smtpserver")
    @Nullable
    public List<SmtpServerConfigBean> getServers();

    public void setServers(@Nullable List<SmtpServerConfigBean> servers);
}
