package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.HandlerChainConfigBean;
import gov.hhs.onc.dcdt.service.mail.james.config.SmtpServerConfigBean;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class SmtpServerConfigBeanImpl extends AbstractJamesConfigBean implements SmtpServerConfigBean {
    private String bind;
    private HandlerChainConfigBean handlerChain;

    @Override
    public boolean hasBind() {
        return !StringUtils.isBlank(this.bind);
    }

    @Nullable
    @Override
    public String getBind() {
        return this.bind;
    }

    @Override
    public void setBind(@Nullable String bind) {
        this.bind = bind;
    }

    @Override
    public HandlerChainConfigBean getHandlerChain() {
        return this.handlerChain;
    }

    @Override
    public void setHandlerChain(HandlerChainConfigBean handlerChain) {
        this.handlerChain = handlerChain;
    }
}
