package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.SmtpServerConfigBean;
import gov.hhs.onc.dcdt.service.mail.james.config.SmtpServersConfigBean;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public class SmtpServersConfigBeanImpl extends AbstractJamesConfigBean implements SmtpServersConfigBean {
    private List<SmtpServerConfigBean> servers;

    @Override
    public boolean hasServers() {
        return !CollectionUtils.isEmpty(this.servers);
    }

    @Nullable
    @Override
    public List<SmtpServerConfigBean> getServers() {
        return this.servers;
    }

    @Override
    public void setServers(@Nullable List<SmtpServerConfigBean> servers) {
        this.servers = servers;
    }
}
