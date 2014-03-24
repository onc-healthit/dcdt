package gov.hhs.onc.dcdt.service.mail.james.mailet;

import org.apache.mailet.Mailet;
import org.apache.mailet.MailetConfig;
import org.springframework.context.ApplicationContextAware;

public interface ToolMailet extends ApplicationContextAware, Mailet, MailetConfig {
    public final static String INIT_PARAM_NAME_MAIL_ENC = "mailEncoding";
}
