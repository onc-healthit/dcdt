package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoSubjectDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import java.security.cert.X509Certificate;
import java.util.Objects;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import org.bouncycastle.asn1.x500.style.BCStyle;

@SupportedValidationTarget({ ValidationTarget.PARAMETERS })
public class CertificateInfoSubjectDnConstraintValidator extends AbstractCertificateInfoConstraintValidator<CertificateInfoSubjectDn> {
    @Override
    protected boolean isValidInternal(MailAddress directAddr, CertificateInfo certInfo, ConstraintValidatorContext validatorContext) throws Exception {
        X509Certificate cert = certInfo.getCertificate();
        CertificateName certSubjName = certInfo.getSubjectName();
        MailAddress certSubjDnDirectAddr, directAddrBound;

        // noinspection ConstantConditions
        if (certSubjName.hasAttribute(BCStyle.EmailAddress)
            && !Objects.equals((certSubjDnDirectAddr = new MailAddressImpl(certSubjName.getAttributeValueString(BCStyle.EmailAddress))), (directAddrBound =
                directAddr.forBindingType(BindingType.ADDRESS)))) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subject Distinguished Name EmailAddress value does not match: %s != %s", certSubjName,
                certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), certSubjDnDirectAddr, directAddrBound));
        }

        return true;
    }
}
