package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.beans.ToolResultBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator.CertificateValidatorConstraintGroup;
import gov.hhs.onc.dcdt.mail.MailAddress;

@CertificateActiveInterval(groups = { CertificateValidatorConstraintGroup.class })
@CertificateRevocationStatus(groups = { CertificateValidatorConstraintGroup.class })
@CertificateSubjectAltNames(groups = { CertificateValidatorConstraintGroup.class })
@CertificateSubjectDn(groups = { CertificateValidatorConstraintGroup.class })
public interface CertificateValidatorContext extends ToolResultBean {
    public CertificateInfo getCertificateInfo();

    public MailAddress getDirectAddress();
}
