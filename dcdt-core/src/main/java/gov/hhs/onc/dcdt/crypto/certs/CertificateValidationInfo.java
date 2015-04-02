package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoValidator.CertificateInfoValidationConstraintGroup;
import gov.hhs.onc.dcdt.mail.MailAddress;

@CertificateInfoActiveInterval(groups = { CertificateInfoValidationConstraintGroup.class })
// @CertificateInfoPath(groups = { CertificateInfoValidationConstraintGroup.class })
@CertificateInfoSubjectAltNames(groups = { CertificateInfoValidationConstraintGroup.class })
@CertificateInfoSubjectDn(groups = { CertificateInfoValidationConstraintGroup.class })
public interface CertificateValidationInfo extends ToolBean {
    public CertificateInfo getCertificateInfo();

    public MailAddress getDirectAddress();
}
