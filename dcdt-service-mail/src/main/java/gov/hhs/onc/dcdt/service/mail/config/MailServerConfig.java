package gov.hhs.onc.dcdt.service.mail.config;

import gov.hhs.onc.dcdt.net.TransportProtocol;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import org.xbill.DNS.Name;

public interface MailServerConfig<T extends TransportProtocol> extends ToolServerConfig<T> {
    public String getGreeting();

    public void setGreeting(String greeting);

    public Name getHeloName();

    public void setHeloName(Name heloName);
}
