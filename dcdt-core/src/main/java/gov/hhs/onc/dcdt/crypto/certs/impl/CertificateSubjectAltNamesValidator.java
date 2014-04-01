package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateAltNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import java.util.Objects;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class CertificateSubjectAltNamesValidator extends AbstractCertificateValidator {
    @Override
    public boolean validate(MailAddress directAddr, CertificateInfo certInfo) throws Exception {
        CertificateName certInfoSubj = certInfo.getSubject();

        // noinspection ConstantConditions
        if (certInfoSubj.hasAltName(CertificateAltNameType.RFC822_NAME)) {
            // noinspection ConstantConditions
            return Objects.equals(new MailAddressImpl(certInfoSubj.getAltName(CertificateAltNameType.RFC822_NAME).getName().toString()), (directAddr =
                directAddr.forBindingType(BindingType.ADDRESS)))
                && (!certInfoSubj.hasAttribute(BCStyle.EmailAddress) || Objects.equals(
                    new MailAddressImpl(certInfoSubj.getAttributeValueString(BCStyle.EmailAddress)), directAddr));
        } else {
            // noinspection ConstantConditions
            return (certInfoSubj.hasAltName(CertificateAltNameType.DNS_NAME) && Objects.equals(
                new MailAddressImpl(certInfoSubj.getAltName(CertificateAltNameType.DNS_NAME).getName().toString()),
                directAddr.forBindingType(BindingType.DOMAIN)));
        }
    }
}
