package gov.hhs.onc.dcdt.mail.config;

import gov.hhs.onc.dcdt.beans.ToolConnectionBean;
import gov.hhs.onc.dcdt.mail.smtp.SmtpTransportProtocol;
import org.xbill.DNS.Name;

public interface MailGatewayConfig extends ToolConnectionBean<SmtpTransportProtocol> {
    public Name getHeloName();

    public void setHeloName(Name heloName);
}
