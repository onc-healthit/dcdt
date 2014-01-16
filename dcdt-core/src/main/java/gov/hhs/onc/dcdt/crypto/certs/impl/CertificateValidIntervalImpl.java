package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateValidInterval;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.MutablePair;

public class CertificateValidIntervalImpl implements CertificateValidInterval {
    private final static int INTERVAL_YEARS_DEFAULT = 100;

    private MutablePair<Date, Date> interval;

    public CertificateValidIntervalImpl() {
        this(new Date());
    }

    public CertificateValidIntervalImpl(Date notBefore) {
        this(notBefore, INTERVAL_YEARS_DEFAULT);
    }

    public CertificateValidIntervalImpl(Date notBefore, int intervalYears) {
        this(notBefore, DateUtils.addYears(notBefore, intervalYears));
    }

    public CertificateValidIntervalImpl(Date notBefore, Date notAfter) {
        this(new MutablePair<>(notBefore, notAfter));
    }

    public CertificateValidIntervalImpl(MutablePair<Date, Date> interval) {
        this.interval = interval;
    }

    @Override
    public boolean isValid(Date date) {
        return date.after(this.getNotBefore()) && date.before(this.getNotAfter());
    }

    @Override
    public Date getNotBefore() {
        return this.interval.getLeft();
    }

    @Override
    public void setNotBefore(Date notBefore) {
        this.interval.setLeft(notBefore);
    }

    @Override
    public Date getNotAfter() {
        return this.interval.getRight();
    }

    @Override
    public void setNotAfter(Date notAfter) {
        this.interval.setRight(notAfter);
    }
}
