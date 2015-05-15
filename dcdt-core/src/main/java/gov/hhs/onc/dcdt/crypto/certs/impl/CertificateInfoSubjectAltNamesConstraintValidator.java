package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateAltNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoSubjectAltNames;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidationInfo;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import java.security.cert.X509Certificate;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class CertificateInfoSubjectAltNamesConstraintValidator extends AbstractCertificateInfoConstraintValidator<CertificateInfoSubjectAltNames> {
    @Override
    protected boolean isValidInternal(CertificateValidationInfo certValidInfo, ConstraintValidatorContext validatorContext) throws Exception {
        CertificateInfo certInfo = certValidInfo.getCertificateInfo();
        X509Certificate cert = certInfo.getCertificate();
        CertificateName certSubjName = certInfo.getSubjectName();
        MailAddress directAddr = certValidInfo.getDirectAddress(), directAddrBound, certSubjAltNameDirectAddr, certSubjDnDirectAddr;

        // noinspection ConstantConditions
        if (!certSubjName.hasAltName(CertificateAltNameType.RFC822_NAME) && !certSubjName.hasAltName(CertificateAltNameType.DNS_NAME)) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension does not contain a rfc822Name or a dNSName",
                    certSubjName, certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName()));
        } else if (certSubjName.hasAltName(CertificateAltNameType.RFC822_NAME)) {
            // noinspection ConstantConditions
            certSubjAltNameDirectAddr = new MailAddressImpl(certSubjName.getAltName(CertificateAltNameType.RFC822_NAME).getName().toString());
            // noinspection ConstantConditions
            if ((directAddrBound = directAddr.forBindingType(BindingType.ADDRESS)) == null
                || !StringUtils.equalsIgnoreCase(certSubjAltNameDirectAddr.toAddress(), directAddrBound.toAddress())) {
                // noinspection ConstantConditions
                throw new CertificateException(String.format(
                    "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension rfc822Name value does not match: %s != %s",
                    certSubjName, certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), certSubjAltNameDirectAddr, directAddrBound));
            } else if (certSubjName.hasAttribute(BCStyle.EmailAddress)
                && !StringUtils.equalsIgnoreCase(certSubjAltNameDirectAddr.toAddress(),
                    (certSubjDnDirectAddr = new MailAddressImpl(certSubjName.getAttributeValueString(BCStyle.EmailAddress))).toAddress())) {
                // noinspection ConstantConditions
                throw new CertificateException(
                    String
                        .format(
                            "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension rfc822Name value does not match subject Distinguished Name EmailAddress value: %s != %s",
                            certSubjName, certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), certSubjAltNameDirectAddr, certSubjDnDirectAddr));
            }
        } else // noinspection ConstantConditions
        if (certSubjName.hasAltName(CertificateAltNameType.DNS_NAME)
            && !StringUtils.equalsIgnoreCase((certSubjAltNameDirectAddr =
                new MailAddressImpl(certSubjName.getAltName(CertificateAltNameType.DNS_NAME).getName().toString())).toAddress(),
                (directAddrBound = directAddr.forBindingType(BindingType.DOMAIN)).toAddress())) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension dNSName value does not match: %s != %s", certSubjName,
                certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), certSubjAltNameDirectAddr, directAddrBound));
        }

        return true;
    }
}
