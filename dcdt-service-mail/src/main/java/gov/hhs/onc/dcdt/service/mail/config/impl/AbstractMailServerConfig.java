package gov.hhs.onc.dcdt.service.mail.config.impl;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.service.config.impl.AbstractToolServerConfig;
import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import org.apache.commons.lang3.ObjectUtils;
import org.xbill.DNS.Name;

public abstract class AbstractMailServerConfig extends AbstractToolServerConfig implements MailServerConfig {
    protected String greeting;
    protected Name heloName;

    protected AbstractMailServerConfig(String protocol) {
        super(protocol);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.heloName == null) {
            this.heloName = ToolDnsNameUtils.fromString(ObjectUtils.defaultIfNull(this.host.getHostName(), this.host.getHostAddress()));
        }

        if (this.greeting == null) {
            this.greeting = this.heloName.toString(true);
        }
    }

    @Override
    public String getGreeting() {
        return this.greeting;
    }

    @Override
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    @Override
    public Name getHeloName() {
        return this.heloName;
    }

    @Override
    public void setHeloName(Name heloName) {
        this.heloName = heloName;
    }
}
