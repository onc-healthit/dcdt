package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.MailProcessorConfigBean;
import gov.hhs.onc.dcdt.service.mail.james.config.MailetConfigBean;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public class MailProcessorConfigBeanImpl extends AbstractJamesConfigBean implements MailProcessorConfigBean {
    private List<MailetConfigBean> mailets;
    private String state;

    @Override
    public boolean hasMailets() {
        return !CollectionUtils.isEmpty(this.mailets);
    }

    @Nullable
    @Override
    public List<MailetConfigBean> getMailets() {
        return this.mailets;
    }

    @Override
    public void setMailets(@Nullable List<MailetConfigBean> mailets) {
        this.mailets = mailets;
    }

    @Override
    public String getState() {
        return this.state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }
}
