package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateSubjectDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidatorContext;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import java.security.cert.X509Certificate;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class CertificateSubjectDnConstraintValidator extends AbstractCertificateConstraintValidator<CertificateSubjectDn> {
    @Override
    protected boolean isValidInternal(CertificateValidatorContext certValidatorContext, ConstraintValidatorContext validatorContext) throws Exception {
        CertificateInfo certInfo = certValidatorContext.getCertificateInfo();
        X509Certificate cert = certInfo.getCertificate();
        CertificateName certSubjName = certInfo.getSubjectName();
        MailAddress directAddr = certValidatorContext.getDirectAddress(), directAddrBound, certSubjDnDirectAddr;

        // noinspection ConstantConditions
        if ((certSubjName.hasAttribute(BCStyle.EmailAddress)
            && (directAddrBound = directAddr.forBindingType(BindingType.ADDRESS)) != null
            && (certSubjDnDirectAddr = new MailAddressImpl(certSubjName.getAttributeValueString(BCStyle.EmailAddress)).forBindingType(BindingType.ADDRESS)) != null && !StringUtils
                .equalsIgnoreCase(certSubjDnDirectAddr.toAddress(), directAddrBound.toAddress()))) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subject Distinguished Name EmailAddress value does not match: %s != %s", certSubjName,
                certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), certSubjDnDirectAddr, directAddrBound));
        }

        return true;
    }
}
