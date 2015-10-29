package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateSubjectDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidatorContext;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class CertificateSubjectDnConstraintValidator extends AbstractCertificateConstraintValidator<CertificateSubjectDn> {
    @Override
    protected boolean isValidInternal(CertificateValidatorContext certValidatorContext, ConstraintValidatorContext validatorContext) throws Exception {
        CertificateInfo certInfo = certValidatorContext.getCertificateInfo();
        CertificateDn certSubjDn = certInfo.getSubjectDn();
        MailAddress directAddr = certValidatorContext.getDirectAddress(), directAddrBound, certSubjDnDirectAddr;

        // noinspection ConstantConditions
        if ((certSubjDn.hasMailAddress() && (directAddrBound = directAddr.forBindingType(BindingType.ADDRESS)) != null
            && (certSubjDnDirectAddr = certSubjDn.getMailAddress()).forBindingType(BindingType.ADDRESS) != null && !StringUtils.equalsIgnoreCase(
            certSubjDnDirectAddr.toAddress(), directAddrBound.toAddress()))) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) subject Distinguished Name EmailAddress value does not match: %s != %s", certSubjDn,
                certInfo.getSerialNumber(), certInfo.getIssuerDn(), certSubjDnDirectAddr, directAddrBound));
        }

        return true;
    }
}
