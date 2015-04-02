package gov.hhs.onc.dcdt.mail.config;

import gov.hhs.onc.dcdt.beans.ToolConnectionBean;
import gov.hhs.onc.dcdt.mail.MailTransportProtocol;
import javax.mail.Session;
import org.springframework.context.ApplicationContextAware;

public interface MailGatewayConfig extends ApplicationContextAware, ToolConnectionBean {
    public Session getSession();

    public MailTransportProtocol getTransportProtocol();

    public void setTransportProtocol(MailTransportProtocol transportProtocol);
}
