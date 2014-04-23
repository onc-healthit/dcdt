package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import gov.hhs.onc.dcdt.config.ConfigurationNode.ConfigurationNodeType;
import gov.hhs.onc.dcdt.service.mail.james.MailProcessorState;
import java.util.List;
import javax.annotation.Nullable;

@ConfigurationNode(name = "processor")
public interface MailProcessorConfigBean extends JamesConfigBean {
    public boolean hasMailets();

    @ConfigurationNode(name = "mailet")
    @Nullable
    public List<MailetConfigBean> getMailets();

    public void setMailets(@Nullable List<MailetConfigBean> mailets);

    public boolean hasMatchers();

    @ConfigurationNode(name = "matcher")
    @Nullable
    public List<MatcherConfigBean> getMatchers();

    public void setMatchers(@Nullable List<MatcherConfigBean> matchers);

    public MailProcessorState getState();

    public void setState(MailProcessorState state);

    @ConfigurationNode(name = "state", type = ConfigurationNodeType.ATTRIBUTE)
    public String getStateString();
}
