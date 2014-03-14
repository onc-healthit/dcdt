package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.mail.MailAddress;

public class CertificateSubjectValidator extends AbstractCertificateValidator {
    @Override
    public boolean validate(MailAddress directAddr, CertificateInfo certInfo) {
        if (certInfo.hasSubject()) {
            CertificateName subj = certInfo.getSubject();
            String directAddrPart = directAddr.toAddress(directAddr.getBindingType());

            // noinspection ConstantConditions
            return !subj.hasMailAddress() || subj.getMailAddress().toAddress().equals(directAddrPart);
        }

        return true;
    }
}
