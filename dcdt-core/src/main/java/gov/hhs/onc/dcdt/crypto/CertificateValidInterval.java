package gov.hhs.onc.dcdt.crypto;

import java.security.cert.CertificateException;
import java.util.Date;

public interface CertificateValidInterval {

    public Date getNotBefore();

    public void setNotBefore(Date notBefore);

    public Date getNotAfter();

    public void setNotAfter(Date notAfter);

    public boolean isValidInterval() throws CertificateException;

    public boolean isValidInterval(Date date) throws CertificateException;
}
