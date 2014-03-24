package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import gov.hhs.onc.dcdt.config.ConfigurationNode.ConfigurationNodeType;
import java.util.List;
import javax.annotation.Nullable;

@ConfigurationNode(name = "processor")
public interface MailProcessorConfigBean extends JamesConfigBean {
    public boolean hasMailets();

    @ConfigurationNode(name = "mailet")
    @Nullable
    public List<MailetConfigBean> getMailets();

    public void setMailets(@Nullable List<MailetConfigBean> mailets);

    @ConfigurationNode(type = ConfigurationNodeType.ATTRIBUTE)
    public String getState();

    public void setState(String state);
}
