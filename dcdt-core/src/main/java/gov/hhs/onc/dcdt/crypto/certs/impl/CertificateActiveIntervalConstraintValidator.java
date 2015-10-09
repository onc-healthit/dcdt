package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateActiveInterval;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidInterval;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidatorContext;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.validation.ConstraintValidatorContext;

public class CertificateActiveIntervalConstraintValidator extends AbstractCertificateConstraintValidator<CertificateActiveInterval> {
    private final static DateFormat DATE_FORMAT_CERT_VALID_INTERVAL_BOUNDARY = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    @Override
    protected boolean isValidInternal(CertificateValidatorContext certValidInfo, ConstraintValidatorContext validatorContext) throws Exception {
        CertificateInfo certInfo = certValidInfo.getCertificateInfo();
        X509Certificate cert = certInfo.getCertificate();
        CertificateValidInterval certValidInterval = certInfo.getValidInterval();

        // noinspection ConstantConditions
        if (!certValidInterval.isValid()) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subj={%s}, serialNum=%s, issuer={%s}) valid interval (notBefore={%s}, notAfter={%s}) is not currently active.", cert
                    .getSubjectX500Principal().getName(), certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(),
                DATE_FORMAT_CERT_VALID_INTERVAL_BOUNDARY.format(certValidInterval.getNotBefore()), DATE_FORMAT_CERT_VALID_INTERVAL_BOUNDARY
                    .format(certValidInterval.getNotAfter())));
        }

        return true;
    }
}
