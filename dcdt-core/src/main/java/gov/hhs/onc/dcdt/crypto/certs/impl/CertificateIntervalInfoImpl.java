package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateIntervalInfo;
import java.util.Date;

public class CertificateIntervalInfoImpl extends AbstractCertificateIntervalDescriptor implements CertificateIntervalInfo {
    private Date notBefore;
    private Date notAfter;

    public CertificateIntervalInfoImpl(Date notBefore, Date notAfter) {
        this.notBefore = notBefore;
        this.notAfter = notAfter;
        this.duration = (this.notAfter.getTime() - this.notBefore.getTime());
    }

    @Override
    public boolean isValid() {
        return this.isValid(new Date());
    }

    @Override
    public boolean isValid(Date date) {
        return date.after(this.notBefore) && date.before(this.notAfter);
    }

    @Override
    public Date getNotBefore() {
        return this.notBefore;
    }

    @Override
    public Date getNotAfter() {
        return this.notAfter;
    }
}
