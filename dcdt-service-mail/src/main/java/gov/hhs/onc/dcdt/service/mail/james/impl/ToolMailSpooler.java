package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.BeanConfigurable;
import gov.hhs.onc.dcdt.service.mail.james.config.MailSpoolerConfigBean;
import org.apache.james.mailetcontainer.impl.JamesMailSpooler;

public class ToolMailSpooler extends JamesMailSpooler implements BeanConfigurable<MailSpoolerConfigBean> {
    private MailSpoolerConfigBean configBean;

    @Override
    public MailSpoolerConfigBean getConfigBean() {
        return this.configBean;
    }

    @Override
    public void setConfigBean(MailSpoolerConfigBean configBean) {
        this.configBean = configBean;
    }
}
