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
import org.bouncycastle.asn1.x509.GeneralName;

public class CertificateInfoSubjectAltNamesConstraintValidator extends AbstractCertificateInfoConstraintValidator<CertificateInfoSubjectAltNames> {
    @Override
    protected boolean isValidInternal(CertificateValidationInfo certValidInfo, ConstraintValidatorContext validatorContext) throws Exception {
        CertificateInfo certInfo = certValidInfo.getCertificateInfo();
        X509Certificate cert = certInfo.getCertificate();
        CertificateName certSubjName = certInfo.getSubjectName();
        MailAddress directAddr = certValidInfo.getDirectAddress(), directAddrBound = null, certSubjAltNameDirectAddr, certSubjDnDirectAddr;

        // noinspection ConstantConditions
        if (!certSubjName.hasAltName(CertificateAltNameType.RFC822_NAME) && !certSubjName.hasAltName(CertificateAltNameType.DNS_NAME)) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension does not contain a rfc822Name or a dNSName",
                certSubjName, certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName()));
        }

        if (certSubjName.hasAltName(CertificateAltNameType.RFC822_NAME) && (directAddrBound = directAddr.forBindingType(BindingType.ADDRESS)) != null) {
            // noinspection ConstantConditions
            for (GeneralName altName : certSubjName.getAltNames(CertificateAltNameType.RFC822_NAME)) {
                if (StringUtils.equalsIgnoreCase((certSubjAltNameDirectAddr = new MailAddressImpl(altName.getName().toString())).toAddress(),
                    directAddrBound.toAddress())) {
                    if (certSubjName.hasAttribute(BCStyle.EmailAddress) && !StringUtils.equalsIgnoreCase(certSubjAltNameDirectAddr.toAddress(),
                        (certSubjDnDirectAddr = new MailAddressImpl(certSubjName.getAttributeValueString(BCStyle.EmailAddress))).toAddress())) {
                        // noinspection ConstantConditions
                        throw new CertificateException(String.format(
                            "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension rfc822Name value does not match subject Distinguished Name EmailAddress value: %s != %s",
                            certSubjName, certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), certSubjAltNameDirectAddr, certSubjDnDirectAddr));
                    }

                    return true;
                }
            }
        }

        // noinspection ConstantConditions
        if (certSubjName.hasAltName(CertificateAltNameType.DNS_NAME)) {
            directAddrBound = directAddr.forBindingType(BindingType.DOMAIN);
            // noinspection ConstantConditions
            for (GeneralName altName : certSubjName.getAltNames(CertificateAltNameType.DNS_NAME)) {
                // noinspection ConstantConditions
                if (StringUtils.equalsIgnoreCase(new MailAddressImpl(altName.getName().toString()).toAddress(), directAddrBound.toAddress())) {
                    return true;
                }
            }
        }

        // noinspection ConstantConditions
        if (directAddrBound.getBindingType() == BindingType.ADDRESS) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension does not contain a rfc822Name value == %s",
                certSubjName, certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), directAddrBound));
        } else {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension does not contain a dNSName value == %s",
                certSubjName, certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), directAddrBound));
        }
    }
}
