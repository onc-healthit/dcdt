package gov.hhs.onc.dcdt.mail.config.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolConnectionBean;
import gov.hhs.onc.dcdt.mail.MailTransportProtocol;
import gov.hhs.onc.dcdt.mail.config.MailGatewayConfig;
import javax.annotation.Nonnegative;
import javax.mail.Session;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class MailGatewayConfigImpl extends AbstractToolConnectionBean implements MailGatewayConfig {
    private AbstractApplicationContext appContext;
    private String mailSessionPlainBeanName;
    private String mailSessionSslBeanName;
    private MailTransportProtocol transportProtocol;

    public MailGatewayConfigImpl(String mailSessionPlainBeanName, String mailSessionSslBeanName) {
        this.mailSessionPlainBeanName = mailSessionPlainBeanName;
        this.mailSessionSslBeanName = mailSessionSslBeanName;
    }

    @Override
    public Session getSession() {
        return this.appContext.getBean((this.isSsl() ? this.mailSessionSslBeanName : this.mailSessionPlainBeanName), Session.class);
        
        // return ToolBeanFactoryUtils.createBean(this.appContext, (this.isSsl() ? this.mailSessionSslBeanName : this.mailSessionPlainBeanName), Session.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Nonnegative
    @Override
    public int getPort() {
        return (this.hasPort() ? super.getPort() : this.transportProtocol.getDefaultPort());
    }

    @Override
    public boolean isSsl() {
        return this.transportProtocol.isSsl();
    }

    @Override
    public MailTransportProtocol getTransportProtocol() {
        return this.transportProtocol;
    }

    @Override
    public void setTransportProtocol(MailTransportProtocol transportProtocol) {
        this.transportProtocol = transportProtocol;
    }
}
