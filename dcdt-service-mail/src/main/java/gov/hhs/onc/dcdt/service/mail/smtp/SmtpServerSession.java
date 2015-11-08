package gov.hhs.onc.dcdt.service.mail.smtp;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommand;
import java.util.LinkedList;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;

public interface SmtpServerSession extends Cloneable {
    public void reset();

    public void resetAuthentication();

    public SmtpServerSession clone() throws CloneNotSupportedException;

    public boolean hasAuthenticatedConfig();

    @Nullable
    public InstanceMailAddressConfig getAuthenticatedConfig();

    public void setAuthenticatedConfig(@Nullable InstanceMailAddressConfig authConfig);

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

    public void setFrom(@Nullable MailAddress from);

    public boolean hasHeloName();

    @Nullable
    public Name getHeloName();

    public void setHeloName(@Nullable Name heloName);

    public boolean hasMimeMessageHelper();

    @Nullable
    public ToolMimeMessageHelper geMimeMessageHelper();

    public void setMimeMessageHelper(@Nullable ToolMimeMessageHelper mimeMsgHelper);

    public boolean hasTo();

    @Nullable
    public MailAddress getTo();

    public void setTo(@Nullable MailAddress to);
}
