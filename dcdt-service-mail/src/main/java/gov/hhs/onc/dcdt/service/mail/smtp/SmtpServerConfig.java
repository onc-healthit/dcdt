package gov.hhs.onc.dcdt.service.mail.smtp;

import gov.hhs.onc.dcdt.mail.smtp.SmtpTransportProtocol;
import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import javax.annotation.Nonnegative;

public interface SmtpServerConfig extends MailServerConfig<SmtpTransportProtocol> {
    @Nonnegative
    public int getCommandReadTimeout();

    public void setCommandReadTimeout(@Nonnegative int cmdReadTimeout);

    @Nonnegative
    public int getDataReadTimeout();

    public void setDataReadTimeout(@Nonnegative int dataReadTimeout);

    @Nonnegative
    public int getMaxCommandFrameLength();

    public void setMaxCommandFrameLength(@Nonnegative int maxCmdFrameLen);

    @Nonnegative
    public int getMaxDataFrameLength();

    public void setMaxDataFrameLength(@Nonnegative int maxDataFrameLen);
}
