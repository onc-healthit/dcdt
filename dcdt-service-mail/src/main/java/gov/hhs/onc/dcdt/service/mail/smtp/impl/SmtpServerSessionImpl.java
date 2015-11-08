package gov.hhs.onc.dcdt.service.mail.smtp.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommand;
import java.util.LinkedList;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;

public class SmtpServerSessionImpl implements SmtpServerSession {
    private InstanceMailAddressConfig authConfig;
    private String authId;
    private String authSecret;
    private LinkedList<SmtpCommand> cmds = new LinkedList<>();
    private MailAddress from;
    private Name heloName;
    private ToolMimeMessageHelper mimeMsgHelper;
    private MailAddress to;

    @Override
    public void reset() {
        this.resetAuthentication();

        this.cmds.clear();
        this.from = null;
        this.heloName = null;
        this.mimeMsgHelper = null;
        this.to = null;
    }

    @Override
    public void resetAuthentication() {
        this.authConfig = null;
        this.authId = null;
        this.authSecret = null;
    }

    @Override
    @SuppressWarnings({ "CloneDoesntCallSuperClone" })
    public SmtpServerSession clone() throws CloneNotSupportedException {
        SmtpServerSession serverSession = new SmtpServerSessionImpl();
        serverSession.setAuthenticatedConfig(this.authConfig);
        serverSession.setAuthenticationId(this.authId);
        serverSession.setAuthenticationSecret(this.authSecret);
        serverSession.setFrom(this.from);
        serverSession.setHeloName(this.heloName);
        serverSession.setMimeMessageHelper(this.mimeMsgHelper);
        serverSession.setTo(this.to);

        return serverSession;
    }

    @Override
    public boolean hasAuthenticatedConfig() {
        return (this.authConfig != null);
    }

    @Nullable
    @Override
    public InstanceMailAddressConfig getAuthenticatedConfig() {
        return this.authConfig;
    }

    @Override
    public void setAuthenticatedConfig(@Nullable InstanceMailAddressConfig authConfig) {
        this.authConfig = authConfig;
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
        return (this.from != null);
    }

    @Nullable
    @Override
    public MailAddress getFrom() {
        return this.from;
    }

    @Override
    public void setFrom(@Nullable MailAddress from) {
        this.from = from;
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
    public boolean hasMimeMessageHelper() {
        return (this.mimeMsgHelper != null);
    }

    @Nullable
    @Override
    public ToolMimeMessageHelper geMimeMessageHelper() {
        return this.mimeMsgHelper;
    }

    @Override
    public void setMimeMessageHelper(@Nullable ToolMimeMessageHelper mimeMsgHelper) {
        this.mimeMsgHelper = mimeMsgHelper;
    }

    @Override
    public boolean hasTo() {
        return (this.to != null);
    }

    @Nullable
    @Override
    public MailAddress getTo() {
        return this.to;
    }

    @Override
    public void setTo(@Nullable MailAddress to) {
        this.to = to;
    }
}
