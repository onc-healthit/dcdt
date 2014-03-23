package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.MailetContextConfigBean;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class MailetContextConfigBeanImpl extends AbstractJamesConfigBean implements MailetContextConfigBean {
    private String postmaster;

    @Override
    public boolean hasPostmaster() {
        return !StringUtils.isBlank(this.postmaster);
    }

    @Nullable
    @Override
    public String getPostmaster() {
        return this.postmaster;
    }

    @Override
    public void setPostmaster(@Nullable String postmaster) {
        this.postmaster = postmaster;
    }
}
