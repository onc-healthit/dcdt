package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.MailProcessorConfigBean;
import gov.hhs.onc.dcdt.service.mail.james.config.MailProcessorsConfigBean;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public class MailProcessorsConfigBeanImpl extends AbstractJamesConfigBean implements MailProcessorsConfigBean {
    private List<MailProcessorConfigBean> procs;

    @Override
    public boolean hasProcessors() {
        return !CollectionUtils.isEmpty(this.procs);
    }

    @Nullable
    @Override
    public List<MailProcessorConfigBean> getProcessors() {
        return this.procs;
    }

    @Override
    public void setProcessors(@Nullable List<MailProcessorConfigBean> procs) {
        this.procs = procs;
    }
}
