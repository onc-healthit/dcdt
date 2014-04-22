package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import javax.annotation.Nullable;

@ConfigurationNode(name = "smtpserver")
public interface SmtpServerConfigBean {
    public boolean hasAuthorizedAddresses();

    @ConfigurationNode
    @Nullable
    public String getAuthorizedAddresses();

    public void setAuthorizedAddresses(@Nullable String authAddrs);

    public boolean hasAuthRequired();

    @ConfigurationNode
    @Nullable
    public String getAuthRequired();

    public void setAuthRequired(@Nullable String authReq);

    public boolean hasBind();

    @ConfigurationNode
    @Nullable
    public String getBind();

    public void setBind(@Nullable String bind);

    @ConfigurationNode(name = "handlerchain")
    public HandlerChainConfigBean getHandlerChain();

    public void setHandlerChain(HandlerChainConfigBean handlerChain);
}
