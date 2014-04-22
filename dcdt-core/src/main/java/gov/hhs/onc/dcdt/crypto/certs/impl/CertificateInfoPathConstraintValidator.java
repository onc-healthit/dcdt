package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoPath;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidationInfo;
import gov.hhs.onc.dcdt.crypto.certs.path.CertificatePathResolver;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.security.cert.X509Certificate;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CertificateInfoPathConstraintValidator extends AbstractCertificateInfoConstraintValidator<CertificateInfoPath> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificatePathResolver certPathResolver;

    @Override
    protected boolean isValidInternal(CertificateValidationInfo certValidInfo, ConstraintValidatorContext validatorContext) throws Exception {
        CertificateInfo certInfo = certValidInfo.getCertificateInfo();
        X509Certificate cert = certInfo.getCertificate();
        MailAddress directAddr = certValidInfo.getDirectAddress(), directAddrBound;

        // TODO: implement

        return true;
    }
}
