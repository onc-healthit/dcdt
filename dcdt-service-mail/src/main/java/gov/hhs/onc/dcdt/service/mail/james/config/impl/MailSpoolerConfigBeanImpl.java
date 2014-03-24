package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.MailSpoolerConfigBean;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import javax.annotation.Nullable;

public class MailSpoolerConfigBeanImpl extends AbstractJamesConfigBean implements MailSpoolerConfigBean {
    private Integer dequeueThreads;
    private Integer threads;

    @Override
    public boolean hasDequeueThreads() {
        return ToolNumberUtils.isPositive(this.dequeueThreads);
    }

    @Nullable
    @Override
    public Integer getDequeueThreads() {
        return this.dequeueThreads;
    }

    @Override
    public void setDequeueThreads(@Nullable Integer dequeueThreads) {
        this.dequeueThreads = dequeueThreads;
    }

    @Override
    public boolean hasThreads() {
        return ToolNumberUtils.isPositive(this.threads);
    }

    @Nullable
    @Override
    public Integer getThreads() {
        return this.threads;
    }

    @Override
    public void setThreads(@Nullable Integer threads) {
        this.threads = threads;
    }
}
