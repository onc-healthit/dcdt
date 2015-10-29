package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateActiveInterval;
import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateIntervalInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidatorContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.validation.ConstraintValidatorContext;

public class CertificateActiveIntervalConstraintValidator extends AbstractCertificateConstraintValidator<CertificateActiveInterval> {
    private final static DateFormat DATE_FORMAT_CERT_INTERVAL_BOUNDARY = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    @Override
    protected boolean isValidInternal(CertificateValidatorContext certValidInfo, ConstraintValidatorContext validatorContext) throws Exception {
        CertificateInfo certInfo = certValidInfo.getCertificateInfo();
        CertificateIntervalInfo certIntervalInfo = certInfo.getInterval();

        // noinspection ConstantConditions
        if (!certIntervalInfo.isValid()) {
            // noinspection ConstantConditions
            throw new CertificateException(String.format(
                "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) interval (notBefore={%s}, notAfter={%s}) is not currently active.",
                certInfo.getSubjectDn(), certInfo.getSerialNumber(), certInfo.getIssuerDn(),
                DATE_FORMAT_CERT_INTERVAL_BOUNDARY.format(certIntervalInfo.getNotBefore()),
                DATE_FORMAT_CERT_INTERVAL_BOUNDARY.format(certIntervalInfo.getNotAfter())));
        }

        return true;
    }
}
