package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.impl.ToolMessageImpl;
import gov.hhs.onc.dcdt.crypto.GeneralNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateSubjectAltNames;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidatorContext;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import gov.hhs.onc.dcdt.utils.ToolMapUtils.ToolMultiValueMap;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertificateSubjectAltNamesConstraintValidator extends AbstractCertificateConstraintValidator<CertificateSubjectAltNames> {
    @Override
    protected boolean isValidInternal(CertificateValidatorContext certValidatorContext, ConstraintValidatorContext validatorContext) throws Exception {
        CertificateInfo certInfo = certValidatorContext.getCertificateInfo();
        CertificateDn certSubjDn = certInfo.getSubjectDn(), certIssuerDn = certInfo.getIssuerDn();

        if (!certInfo.hasSubjectAltNames()) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) does not contain a subjectAltName X509v3 extension.", certSubjDn,
                certInfo.getSerialNumber(), certIssuerDn));
        }

        ToolMultiValueMap<GeneralNameType, ASN1Encodable> certSubjAltNames = certInfo.getSubjectAltNames();
        // noinspection ConstantConditions
        boolean certSubjAltNamesAddrAvailable = !certSubjAltNames.isEmpty(GeneralNameType.RFC822_NAME), certSubjAltNamesDomainAvailable =
            !certSubjAltNames.isEmpty(GeneralNameType.DNS_NAME);
        MailAddress directAddr = certValidatorContext.getDirectAddress(), directAddrBound = null, certSubjAltNameDirectAddr, certSubjDnDirectAddr;

        if (!certSubjAltNamesAddrAvailable && !certSubjAltNamesDomainAvailable) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) subjectAltName X509v3 extension does not contain a rfc822Name or a dNSName.",
                certSubjDn, certInfo.getSerialNumber(), certIssuerDn));
        }

        if (certSubjAltNamesAddrAvailable && certSubjAltNamesDomainAvailable) {
            // noinspection ConstantConditions
            certValidatorContext.getMessages().add(
                new ToolMessageImpl(ToolMessageLevel.WARN, String.format(
                    "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) subjectAltName X509v3 extension contains multiple rfc822 and/or dNSName values.",
                    certSubjDn, certInfo.getSerialNumber(), certIssuerDn)));
        }

        if (certSubjAltNamesAddrAvailable && (directAddrBound = directAddr.forBindingType(BindingType.ADDRESS)) != null) {
            // noinspection ConstantConditions
            for (ASN1Encodable altName : certSubjAltNames.getCollection(GeneralNameType.RFC822_NAME)) {
                if (StringUtils
                    .equalsIgnoreCase((certSubjAltNameDirectAddr = new MailAddressImpl(altName.toString())).toAddress(), directAddrBound.toAddress())) {
                    // noinspection ConstantConditions
                    if (certSubjDn.hasMailAddress()
                        && !StringUtils.equalsIgnoreCase(certSubjAltNameDirectAddr.toAddress(),
                            (certSubjDnDirectAddr = certSubjDn.getMailAddress()).toAddress())) {
                        // noinspection ConstantConditions
                        throw new CertificateException(
                            String
                                .format(
                                    "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) subjectAltName X509v3 extension rfc822Name value does not match subject Distinguished Name EmailAddress value: %s != %s",
                                    certSubjDn, certInfo.getSerialNumber(), certIssuerDn, certSubjAltNameDirectAddr, certSubjDnDirectAddr));
                    }

                    return true;
                }
            }
        }

        // noinspection ConstantConditions
        if (certSubjAltNamesDomainAvailable) {
            directAddrBound = directAddr.forBindingType(BindingType.DOMAIN);
            // noinspection ConstantConditions
            for (ASN1Encodable altName : certSubjAltNames.getCollection(GeneralNameType.DNS_NAME)) {
                // noinspection ConstantConditions
                if (StringUtils.equalsIgnoreCase(new MailAddressImpl(altName.toString()).toAddress(), directAddrBound.toAddress())) {
                    return true;
                }
            }
        }

        // noinspection ConstantConditions
        if (directAddrBound.getBindingType() == BindingType.ADDRESS) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) subjectAltName X509v3 extension does not contain a rfc822Name value == %s", certSubjDn,
                certInfo.getSerialNumber(), certIssuerDn, directAddrBound));
        } else {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) subjectAltName X509v3 extension does not contain a dNSName value == %s", certSubjDn,
                certInfo.getSerialNumber(), certIssuerDn, directAddrBound));
        }
    }
}
