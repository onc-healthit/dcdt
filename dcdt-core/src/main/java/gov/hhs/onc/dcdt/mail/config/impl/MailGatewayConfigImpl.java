package gov.hhs.onc.dcdt.mail.config.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolConnectionBean;
import gov.hhs.onc.dcdt.mail.config.MailGatewayConfig;
import gov.hhs.onc.dcdt.mail.smtp.SmtpTransportProtocol;
import org.xbill.DNS.Name;

public class MailGatewayConfigImpl extends AbstractToolConnectionBean<SmtpTransportProtocol> implements MailGatewayConfig {
    protected Name heloName;

    @Override
    public Name getHeloName() {
        return this.heloName;
    }

    @Override
    public void setHeloName(Name heloName) {
        this.heloName = heloName;
    }
}
