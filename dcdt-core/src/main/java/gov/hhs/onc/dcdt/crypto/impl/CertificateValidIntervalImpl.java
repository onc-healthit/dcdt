package gov.hhs.onc.dcdt.crypto.impl;

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
    public boolean isValidInterval() {
        return isValidInterval(new Date());
    }

    @Override
    public boolean isValidInterval(Date date) {
        return date.after(this.notBefore) && date.before(this.notAfter);
    }
}
