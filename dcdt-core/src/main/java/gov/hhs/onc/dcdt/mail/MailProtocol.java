package gov.hhs.onc.dcdt.mail;

import javax.annotation.Nonnegative;

public interface MailProtocol {
    @Nonnegative
    public int getDefaultPort();

    public String getProtocol();

    public boolean isSsl();
}
