package gov.hhs.onc.dcdt.config.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.config.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;

@JsonTypeName("instanceMailAddrConfig")
public class InstanceMailAddressConfigImpl extends AbstractToolNamedBean implements InstanceMailAddressConfig {
    private MailAddress mailAddr;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!this.mailAddr.hasLocalPart()) {
            this.mailAddr.setLocalPart(this.name);
        }
    }

    @Override
    public MailAddress getMailAddress() {
        return this.mailAddr;
    }

    @Override
    public void setMailAddress(MailAddress mailAddr) {
        this.mailAddr = mailAddr;
    }
}
