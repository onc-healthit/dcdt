package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.MailetConfigBean;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;

public class MailetConfigBeanImpl extends AbstractJamesConfigBean implements MailetConfigBean {
    private String className;
    private String match;
    private Map<String, ?> props;

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String getMatch() {
        return this.match;
    }

    @Override
    public void setMatch(String match) {
        this.match = match;
    }

    @Override
    public boolean hasProperties() {
        return !MapUtils.isEmpty(this.props);
    }

    @Nullable
    @Override
    public Map<String, ?> getProperties() {
        return this.props;
    }

    @Override
    public void setProperties(@Nullable Map<String, ?> props) {
        this.props = props;
    }
}
