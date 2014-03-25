package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.service.mail.james.config.BeanConfigurable;
import gov.hhs.onc.dcdt.service.mail.james.config.MailProcessorsConfigBean;
import org.apache.james.mailetcontainer.api.MailProcessor;

public interface ToolMailProcessor extends BeanConfigurable<MailProcessorsConfigBean>, MailProcessor {
}
