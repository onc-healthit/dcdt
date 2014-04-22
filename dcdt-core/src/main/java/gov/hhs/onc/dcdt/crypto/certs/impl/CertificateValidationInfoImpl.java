package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidationInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("certValidInfoImpl")
@Lazy
@Scope("prototype")
public class CertificateValidationInfoImpl extends AbstractToolBean implements CertificateValidationInfo {
    private CertificateInfo certInfo;
    private MailAddress directAddr;

    public CertificateValidationInfoImpl(MailAddress directAddr, CertificateInfo certInfo) {
        this.directAddr = directAddr;
        this.certInfo = certInfo;
    }

    @Override
    public CertificateInfo getCertificateInfo() {
        return this.certInfo;
    }

    @Override
    public MailAddress getDirectAddress() {
        return this.directAddr;
    }
}
