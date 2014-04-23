package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.MatcherConfigBean;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class MatcherConfigBeanImpl extends AbstractJamesConfigBean implements MatcherConfigBean {
    private String name;
    private String match;
    private String notMatch;

    @Override
    public boolean hasName() {
        return !StringUtils.isBlank(this.name);
    }

    @Nullable
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Override
    public boolean hasMatch() {
        return !StringUtils.isBlank(this.match);
    }

    @Nullable
    @Override
    public String getMatch() {
        return this.match;
    }

    @Override
    public void setMatch(@Nullable String match) {
        this.match = match;
    }

    @Override
    public boolean hasNotMatch() {
        return !StringUtils.isBlank(this.notMatch);
    }

    @Nullable
    @Override
    public String getNotMatch() {
        return this.notMatch;
    }

    @Override
    public void setNotMatch(@Nullable String notMatch) {
        this.notMatch = notMatch;
    }
}
