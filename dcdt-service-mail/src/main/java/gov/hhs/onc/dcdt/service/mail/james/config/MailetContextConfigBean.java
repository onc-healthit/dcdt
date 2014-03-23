package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import javax.annotation.Nullable;

@ConfigurationNode(name = "context")
public interface MailetContextConfigBean extends JamesConfigBean {
    public boolean hasPostmaster();

    @ConfigurationNode
    @Nullable
    public String getPostmaster();

    public void setPostmaster(@Nullable String postmaster);
}
