package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.Date;

public class CertificateValidIntervalValidator extends AbstractCertificateValidator {
    @Override
    public boolean validate(MailAddress directAddr, CertificateInfo certInfo) {
        // noinspection ConstantConditions
        return certInfo.getValidInterval().isValid(new Date());
    }
}
