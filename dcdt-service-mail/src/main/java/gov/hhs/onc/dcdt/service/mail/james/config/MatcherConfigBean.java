package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import gov.hhs.onc.dcdt.config.ConfigurationNode.ConfigurationNodeType;
import javax.annotation.Nullable;

@ConfigurationNode(name = "matcher")
public interface MatcherConfigBean extends JamesConfigBean {
    public boolean hasName();

    @ConfigurationNode(type = ConfigurationNodeType.ATTRIBUTE)
    @Nullable
    public String getName();

    public void setName(@Nullable String name);

    public boolean hasMatch();

    @ConfigurationNode(type = ConfigurationNodeType.ATTRIBUTE)
    @Nullable
    public String getMatch();

    public void setMatch(@Nullable String match);

    public boolean hasNotMatch();

    @ConfigurationNode(name = "notmatch", type = ConfigurationNodeType.ATTRIBUTE)
    @Nullable
    public String getNotMatch();

    public void setNotMatch(@Nullable String notMatch);
}
