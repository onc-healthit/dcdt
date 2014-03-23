package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import gov.hhs.onc.dcdt.config.ConfigurationNode.ConfigurationNodeType;
import java.util.Map;
import javax.annotation.Nullable;

@ConfigurationNode(name = "mailet")
public interface MailetConfigBean extends JamesConfigBean {
    @ConfigurationNode(name = "class", type = ConfigurationNodeType.ATTRIBUTE)
    public String getClassName();

    public void setClassName(String className);

    @ConfigurationNode(type = ConfigurationNodeType.ATTRIBUTE)
    public String getMatch();

    public void setMatch(String match);

    public boolean hasProperties();

    @ConfigurationNode(type = ConfigurationNodeType.CHILD_MAP)
    @Nullable
    public Map<String, ?> getProperties();

    public void setProperties(@Nullable Map<String, ?> props);
}
