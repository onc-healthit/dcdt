package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.BeanConfigurable;
import gov.hhs.onc.dcdt.service.mail.james.config.SmtpServersConfigBean;
import org.apache.james.smtpserver.netty.SMTPServerFactory;

public class ToolSmtpServerFactory extends SMTPServerFactory implements BeanConfigurable<SmtpServersConfigBean> {
    private SmtpServersConfigBean configBean;

    @Override
    public SmtpServersConfigBean getConfigBean() {
        return this.configBean;
    }

    @Override
    public void setConfigBean(SmtpServersConfigBean configBean) {
        this.configBean = configBean;
    }
}
