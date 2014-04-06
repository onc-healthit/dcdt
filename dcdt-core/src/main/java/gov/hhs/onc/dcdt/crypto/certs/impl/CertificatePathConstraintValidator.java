package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificatePath;
import gov.hhs.onc.dcdt.crypto.certs.path.CertificatePathResolver;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.security.cert.X509Certificate;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import org.springframework.beans.factory.annotation.Autowired;

@SupportedValidationTarget({ ValidationTarget.PARAMETERS })
public class CertificatePathConstraintValidator extends AbstractCertificateInfoConstraintValidator<CertificatePath> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificatePathResolver certPathResolver;

    @Override
    protected boolean isValidInternal(MailAddress directAddr, CertificateInfo certInfo, ConstraintValidatorContext validatorContext) throws Exception {
        X509Certificate cert = certInfo.getCertificate();

        

        return true;
    }
}
