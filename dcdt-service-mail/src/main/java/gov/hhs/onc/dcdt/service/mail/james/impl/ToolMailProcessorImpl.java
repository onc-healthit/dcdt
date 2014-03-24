package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.service.mail.james.ToolMailProcessor;
import gov.hhs.onc.dcdt.service.mail.james.config.MailProcessorsConfigBean;
import org.apache.james.mailetcontainer.impl.camel.CamelCompositeProcessor;

public class ToolMailProcessorImpl extends CamelCompositeProcessor implements ToolMailProcessor {
    private MailProcessorsConfigBean configBean;

    @Override
    public MailProcessorsConfigBean getConfigBean() {
        return this.configBean;
    }

    @Override
    public void setConfigBean(MailProcessorsConfigBean configBean) {
        this.configBean = configBean;
    }
}
