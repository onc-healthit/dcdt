package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.service.mail.james.config.BeanConfigurable;
import gov.hhs.onc.dcdt.service.mail.james.config.MailetContextConfigBean;
import org.apache.mailet.MailetContext;

public interface ToolMailetContext extends BeanConfigurable<MailetContextConfigBean>, MailetContext {
}
