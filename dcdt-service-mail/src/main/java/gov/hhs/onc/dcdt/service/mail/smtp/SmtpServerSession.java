package gov.hhs.onc.dcdt.service.mail.smtp;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommand;
import java.util.LinkedList;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;

public interface SmtpServerSession {
    public void reset();

    public void resetAuthentication();

    public boolean hasAuthenticatedAddressConfig();

    @Nullable
    public InstanceMailAddressConfig getAuthenticatedAddressConfig();

    public void setAuthenticatedAddressConfig(@Nullable InstanceMailAddressConfig authAddrConfig);

    public boolean hasAuthenticationId();

    @Nullable
    public String getAuthenticationId();

    public void setAuthenticationId(@Nullable String authId);

    public boolean hasAuthenticationSecret();

    @Nullable
    public String getAuthenticationSecret();

    public void setAuthenticationSecret(@Nullable String authSecret);

    public boolean hasCommands();

    public LinkedList<SmtpCommand> getCommands();

    public boolean hasFrom();

    @Nullable
    public MailAddress getFrom();

    public void setFrom(@Nullable MailAddress fromAddr);

    public boolean hasFromConfig();

    @Nullable
    public InstanceMailAddressConfig getFromConfig();

    public void setFromConfig(@Nullable InstanceMailAddressConfig fromConfig);

    public boolean hasHeloName();

    @Nullable
    public Name getHeloName();

    public void setHeloName(@Nullable Name heloName);

    public boolean hasMailInfo();

    @Nullable
    public MailInfo getMailInfo();

    public void setMailInfo(@Nullable MailInfo mailInfo);

    public boolean hasTo();

    @Nullable
    public MailAddress getTo();

    public void setTo(@Nullable MailAddress toAddr);

    public boolean hasToConfig();

    @Nullable
    public InstanceMailAddressConfig getToConfig();

    public void setToConfig(@Nullable InstanceMailAddressConfig toConfig);
}
