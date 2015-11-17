package gov.hhs.onc.dcdt.service.mail.smtp.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import java.util.LinkedList;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;

public class SmtpServerSessionImpl implements SmtpServerSession {
    private InstanceMailAddressConfig authAddrConfig;
    private String authId;
    private String authSecret;
    private LinkedList<SmtpCommand> cmds = new LinkedList<>();
    private MailAddress fromAddr;
    private InstanceMailAddressConfig fromConfig;
    private Name heloName;
    private MailInfo mailInfo;
    private MailAddress toAddr;
    private InstanceMailAddressConfig toConfig;

    @Override
    public void reset() {
        this.resetAuthentication();

        this.cmds.clear();
        this.fromAddr = null;
        this.fromConfig = null;
        this.heloName = null;
        this.mailInfo = null;
        this.toAddr = null;
        this.toConfig = null;
    }

    @Override
    public void resetAuthentication() {
        this.authAddrConfig = null;
        this.authId = null;
        this.authSecret = null;
    }

    @Override
    public boolean hasAuthenticatedAddressConfig() {
        return (this.authAddrConfig != null);
    }

    @Nullable
    @Override
    public InstanceMailAddressConfig getAuthenticatedAddressConfig() {
        return this.authAddrConfig;
    }

    @Override
    public void setAuthenticatedAddressConfig(@Nullable InstanceMailAddressConfig authAddrConfig) {
        this.authAddrConfig = authAddrConfig;
    }

    @Override
    public boolean hasAuthenticationId() {
        return (this.authId != null);
    }

    @Nullable
    @Override
    public String getAuthenticationId() {
        return this.authId;
    }

    @Override
    public void setAuthenticationId(@Nullable String authId) {
        this.authId = authId;
    }

    @Override
    public boolean hasAuthenticationSecret() {
        return (this.authSecret != null);
    }

    @Nullable
    @Override
    public String getAuthenticationSecret() {
        return this.authSecret;
    }

    @Override
    public void setAuthenticationSecret(@Nullable String authSecret) {
        this.authSecret = authSecret;
    }

    @Override
    public boolean hasCommands() {
        return !this.cmds.isEmpty();
    }

    @Override
    public LinkedList<SmtpCommand> getCommands() {
        return this.cmds;
    }

    @Override
    public boolean hasFrom() {
        return (this.fromAddr != null);
    }

    @Nullable
    @Override
    public MailAddress getFrom() {
        return this.fromAddr;
    }

    @Override
    public void setFrom(@Nullable MailAddress fromAddr) {
        this.fromAddr = fromAddr;
    }

    @Override
    public boolean hasFromConfig() {
        return (this.fromConfig != null);
    }

    @Nullable
    @Override
    public InstanceMailAddressConfig getFromConfig() {
        return this.fromConfig;
    }

    @Override
    public void setFromConfig(@Nullable InstanceMailAddressConfig fromConfig) {
        this.fromConfig = fromConfig;
    }

    @Override
    public boolean hasHeloName() {
        return (this.heloName != null);
    }

    @Nullable
    @Override
    public Name getHeloName() {
        return this.heloName;
    }

    @Override
    public void setHeloName(@Nullable Name heloName) {
        this.heloName = heloName;
    }

    @Override
    public boolean hasMailInfo() {
        return (this.mailInfo != null);
    }

    @Nullable
    @Override
    public MailInfo getMailInfo() {
        return this.mailInfo;
    }

    @Override
    public void setMailInfo(@Nullable MailInfo mailInfo) {
        this.mailInfo = mailInfo;
    }

    @Override
    public boolean hasTo() {
        return (this.toAddr != null);
    }

    @Nullable
    @Override
    public MailAddress getTo() {
        return this.toAddr;
    }

    @Override
    public void setTo(@Nullable MailAddress toAddr) {
        this.toAddr = toAddr;
    }

    @Override
    public boolean hasToConfig() {
        return (this.toConfig != null);
    }

    @Nullable
    @Override
    public InstanceMailAddressConfig getToConfig() {
        return this.toConfig;
    }

    @Override
    public void setToConfig(@Nullable InstanceMailAddressConfig toConfig) {
        this.toConfig = toConfig;
    }
}
