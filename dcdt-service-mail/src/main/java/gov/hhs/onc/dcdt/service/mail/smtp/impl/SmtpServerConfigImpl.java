package gov.hhs.onc.dcdt.service.mail.smtp.impl;

import gov.hhs.onc.dcdt.service.mail.config.impl.AbstractMailServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import javax.annotation.Nonnegative;

public class SmtpServerConfigImpl extends AbstractMailServerConfig implements SmtpServerConfig {
    private int cmdReadTimeout;
    private int dataReadTimeout;
    private int maxCmdFrameLen;
    private int maxDataFrameLen;

    public SmtpServerConfigImpl() {
        super("SMTP");
    }

    @Nonnegative
    @Override
    public int getCommandReadTimeout() {
        return this.cmdReadTimeout;
    }

    @Override
    public void setCommandReadTimeout(@Nonnegative int cmdReadTimeout) {
        this.cmdReadTimeout = cmdReadTimeout;
    }

    @Nonnegative
    @Override
    public int getDataReadTimeout() {
        return this.dataReadTimeout;
    }

    @Override
    public void setDataReadTimeout(@Nonnegative int dataReadTimeout) {
        this.dataReadTimeout = dataReadTimeout;
    }

    @Nonnegative
    @Override
    public int getMaxCommandFrameLength() {
        return this.maxCmdFrameLen;
    }

    @Override
    public void setMaxCommandFrameLength(@Nonnegative int maxCmdFrameLen) {
        this.maxCmdFrameLen = maxCmdFrameLen;
    }

    @Nonnegative
    @Override
    public int getMaxDataFrameLength() {
        return this.maxDataFrameLen;
    }

    @Override
    public void setMaxDataFrameLength(@Nonnegative int maxDataFrameLen) {
        this.maxDataFrameLen = maxDataFrameLen;
    }
}
