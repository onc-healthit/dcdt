package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import java.util.Objects;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class CertificateSubjectValidator extends AbstractCertificateValidator {
    @Override
    public boolean validate(MailAddress directAddr, CertificateInfo certInfo) throws Exception {
        CertificateName certInfoSubj = certInfo.getSubject();

        // noinspection ConstantConditions
        return (!certInfoSubj.hasAttribute(BCStyle.EmailAddress) || Objects.equals(
            new MailAddressImpl(certInfoSubj.getAttributeValueString(BCStyle.EmailAddress)), directAddr.forBindingType(BindingType.ADDRESS)));
    }
}
