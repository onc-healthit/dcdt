package gov.hhs.onc.dcdt.crypto.impl;

import java.security.cert.CertificateException;
import java.util.Date;

import gov.hhs.onc.dcdt.crypto.CertificateValidInterval;

public class CertificateValidIntervalImpl implements CertificateValidInterval {
    private Date notBefore;
    private Date notAfter;

    public CertificateValidIntervalImpl(Date notBefore, Date notAfter) {
        this.notBefore = notBefore;
        this.notAfter = notAfter;
    }

    @Override
    public Date getNotBefore() {
        return this.notBefore;
    }

    @Override
    public void setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
    }

    @Override
    public Date getNotAfter() {
        return this.notAfter;
    }

    @Override
    public void setNotAfter(Date notAfter) {
        this.notAfter = notAfter;
    }

    @Override
    public boolean isValidInterval() throws CertificateException {
        return isValidInterval(new Date());
    }

    @Override
    public boolean isValidInterval(Date date) throws CertificateException {
        if (date.before(this.notBefore)) {
            throw new CertificateException("Certificate not yet valid.");
        } else if (date.after(this.notAfter)) {
            throw new CertificateException("Certificate expired.");
        } else {
            return true;
        }
    }
}
