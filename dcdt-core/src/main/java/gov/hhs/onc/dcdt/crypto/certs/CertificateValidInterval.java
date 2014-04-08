package gov.hhs.onc.dcdt.crypto.certs;

import java.util.Date;

public interface CertificateValidInterval {
    public boolean isValid();

    public boolean isValid(Date date);

    public Date getNotBefore();

    public void setNotBefore(Date notBefore);

    public Date getNotAfter();

    public void setNotAfter(Date notAfter);
}
