package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateAltNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoSubjectAltNames;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import java.security.cert.X509Certificate;
import java.util.Objects;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import org.bouncycastle.asn1.x500.style.BCStyle;

@SupportedValidationTarget({ ValidationTarget.PARAMETERS })
public class CertificateInfoSubjectAltNamesConstraintValidator extends AbstractCertificateInfoConstraintValidator<CertificateInfoSubjectAltNames> {
    @Override
    protected boolean isValidInternal(MailAddress directAddr, CertificateInfo certInfo, ConstraintValidatorContext validatorContext) throws Exception {
        X509Certificate cert = certInfo.getCertificate();
        CertificateName certSubjName = certInfo.getSubjectName();
        MailAddress certSubjAltNameDirectAddr, certSubjDnDirectAddr, directAddrBound;

        // noinspection ConstantConditions
        if (certSubjName.hasAltName(CertificateAltNameType.RFC822_NAME)) {
            // noinspection ConstantConditions
            if (!Objects.equals((certSubjAltNameDirectAddr =
                new MailAddressImpl(certSubjName.getAltName(CertificateAltNameType.RFC822_NAME).getName().toString())),
                (directAddrBound = directAddr.forBindingType(BindingType.ADDRESS)))) {
                // noinspection ConstantConditions
                throw new CertificateException(String.format(
                    "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension rfc822Name value does not match: %s != %s",
                    certSubjName, certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), certSubjAltNameDirectAddr, directAddrBound));
            } else if (certSubjName.hasAttribute(BCStyle.EmailAddress)
                && !Objects.equals(certSubjAltNameDirectAddr,
                    (certSubjDnDirectAddr = new MailAddressImpl(certSubjName.getAttributeValueString(BCStyle.EmailAddress))))) {
                // noinspection ConstantConditions
                throw new CertificateException(
                    String
                        .format(
                            "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension rfc822Name value does not match subject Distinguished Name EmailAddress value: %s != %s",
                            certSubjName, certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), certSubjAltNameDirectAddr, certSubjDnDirectAddr));
            }
        } else // noinspection ConstantConditions
        if (certSubjName.hasAltName(CertificateAltNameType.DNS_NAME)
            && !Objects.equals(
                (certSubjAltNameDirectAddr = new MailAddressImpl(certSubjName.getAltName(CertificateAltNameType.DNS_NAME).getName().toString())),
                (directAddrBound = directAddr.forBindingType(BindingType.DOMAIN)))) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension dNSName value does not match: %s != %s", certSubjName,
                certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), certSubjAltNameDirectAddr, directAddrBound));
        }

        return true;
    }
}
