package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.MailetConfigBean;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;

public class MailetConfigBeanImpl extends AbstractJamesConfigBean implements MailetConfigBean {
    private String className;
    private Map<String, String> initParams;
    private String match;

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public boolean hasInitParameters() {
        return !MapUtils.isEmpty(this.initParams);
    }

    @Nullable
    @Override
    public Map<String, String> getInitParameters() {
        return this.initParams;
    }

    @Override
    public void setInitParameters(@Nullable Map<String, String> initParams) {
        this.initParams = initParams;
    }

    @Override
    public String getMatch() {
        return this.match;
    }

    @Override
    public void setMatch(String match) {
        this.match = match;
    }
}
