package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.security.cert.CertificateParsingException;
import java.util.Collection;
import java.util.List;
import org.bouncycastle.asn1.x509.GeneralName;

public class CertificateSubjectAltNamesValidator extends AbstractCertificateValidator {
    /**
     * Information regarding certificate validation regarding the subjectAltName extension found in: <a href=
     * "http://wiki.directproject.org/Applicability+Statement+for+Secure+Health+Transport+Working+Version#x4.0 Trust Verification-4.1 Verification of Certificate-Entity Binding"
     * > Direct Project Applicability Statement for Secure Health Transport - 4.1 Verification of Certificate-Entity Binding </a>
     * 
     * For the purposes of encryption or signature verification, the STA MUST verify the address or domain that an X.509 certificate is purported to be issued
     * to by following the guidance in sections 4.1.2.6 and 4.2.1.7 of RFC 5280:
     * 
     * 4.1.1 Subject Verification of Direct Address Bound Certificates The following conditions MUST be true for a Direct Address Bound Certificate
     * 
     * If the subjectAltName extension is present and an rfc822Name is included then it contains the e-mail address.
     * 
     * If the Subject Distinguished Name contains an EmailAddress legacy attribute, then it contains the e-mail address. If both of the previous locations
     * contain an e-mail address, they must match
     * 
     * 4.1.2 Subject Verification for Organizationally-Bound Certificates The following condition MUST be true for an Organizationally-Bound Certificate:
     * 
     * The subjectAltName extension is present, a dNSName is included, and it matches the Direct Address' Health Internet Domain.
     */
    @Override
    public boolean validate(MailAddress directAddr, CertificateInfo certInfo) {
        int nameType = getGeneralName(directAddr);

        return (nameType == GeneralName.rfc822Name || nameType == GeneralName.dNSName)
            && verifySubjAltName(certInfo, nameType, directAddr.toAddress(directAddr.getBindingType()));
    }

    private int getGeneralName(MailAddress directAddr) {
        int nameType;

        switch (directAddr.getBindingType()) {
            case ADDRESS:
                nameType = GeneralName.rfc822Name;
                break;
            case DOMAIN:
                nameType = GeneralName.dNSName;
                break;
            default:
                nameType = GeneralName.otherName;
                break;
        }

        return nameType;
    }

    private boolean verifySubjAltName(CertificateInfo certInfo, int nameType, String directAddr) {
        Collection<List<?>> subjAltNames;
        try {
            // noinspection ConstantConditions
            subjAltNames = certInfo.getCertificate().getSubjectAlternativeNames();
        } catch (CertificateParsingException e) {
            return false;
        }

        if (subjAltNames != null) {
            for (List<?> generalName : subjAltNames) {
                if ((Integer) generalName.get(0) == nameType && !directAddr.equals(generalName.get(1))) {
                    return false;
                }
            }
        }

        return true;
    }
}
