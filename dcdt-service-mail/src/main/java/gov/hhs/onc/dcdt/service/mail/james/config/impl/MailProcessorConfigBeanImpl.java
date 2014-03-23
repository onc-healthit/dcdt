package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.MailProcessorState;
import gov.hhs.onc.dcdt.service.mail.james.config.MailProcessorConfigBean;
import gov.hhs.onc.dcdt.service.mail.james.config.MailetConfigBean;
import gov.hhs.onc.dcdt.service.mail.james.config.MatcherConfigBean;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public class MailProcessorConfigBeanImpl extends AbstractJamesConfigBean implements MailProcessorConfigBean {
    private List<MailetConfigBean> mailets;
    private List<MatcherConfigBean> matchers;
    private MailProcessorState state;

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
    public boolean hasMatchers() {
        return !CollectionUtils.isEmpty(this.matchers);
    }

    @Nullable
    @Override
    public List<MatcherConfigBean> getMatchers() {
        return this.matchers;
    }

    @Override
    public void setMatchers(@Nullable List<MatcherConfigBean> matchers) {
        this.matchers = matchers;
    }

    @Override
    public MailProcessorState getState() {
        return this.state;
    }

    @Override
    public void setState(MailProcessorState state) {
        this.state = state;
    }

    @Override
    public String getStateString() {
        return this.state.getState();
    }
}
