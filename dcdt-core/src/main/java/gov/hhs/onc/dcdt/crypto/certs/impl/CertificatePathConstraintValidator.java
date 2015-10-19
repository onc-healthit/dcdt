package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificatePath;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidatorContext;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.security.cert.X509Certificate;
import javax.validation.ConstraintValidatorContext;

public class CertificatePathConstraintValidator extends AbstractCertificateConstraintValidator<CertificatePath> {
    // @formatter:off
    /*
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificatePathResolver certPathResolver;
    */
    // @formatter:on

    @Override
    protected boolean isValidInternal(CertificateValidatorContext certValidatorContext, ConstraintValidatorContext validatorContext) throws Exception {
        CertificateInfo certInfo = certValidatorContext.getCertificateInfo();
        X509Certificate cert = certInfo.getCertificate();
        MailAddress directAddr = certValidatorContext.getDirectAddress(), directAddrBound;

        // TODO: implement

        return true;
    }
}
