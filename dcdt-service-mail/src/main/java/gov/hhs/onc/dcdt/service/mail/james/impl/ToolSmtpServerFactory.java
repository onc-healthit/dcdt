package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.ToolRuntimeException;
import gov.hhs.onc.dcdt.service.mail.james.config.BeanConfigurable;
import gov.hhs.onc.dcdt.service.mail.james.config.SmtpServersConfigBean;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import javax.management.InstanceAlreadyExistsException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.james.smtpserver.netty.SMTPServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolSmtpServerFactory extends SMTPServerFactory implements BeanConfigurable<SmtpServersConfigBean> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ToolSmtpServerFactory.class);

    private SmtpServersConfigBean configBean;

    @Override
    public SmtpServersConfigBean getConfigBean() {
        return this.configBean;
    }

    @Override
    public void setConfigBean(SmtpServersConfigBean configBean) {
        this.configBean = configBean;
    }

    @Override
    public void init() throws Exception {
        try {
            super.init();
        } catch (Throwable th) {
            if (ExceptionUtils.indexOfThrowable(th, InstanceAlreadyExistsException.class) == -1) {
                throw new ToolRuntimeException(String.format("Unable to initialize SMTP server factory (name=%s) managed bean.", ToolClassUtils.getName(this)),
                    th);
            }

            LOGGER.error(String.format("Unable to register SMTP server factory (name=%s) managed bean.", ToolClassUtils.getName(this)), th.getMessage());
        }
    }
}
