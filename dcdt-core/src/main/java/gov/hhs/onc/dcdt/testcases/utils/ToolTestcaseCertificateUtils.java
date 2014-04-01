package gov.hhs.onc.dcdt.testcases.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ToolTestcaseCertificateUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(ToolTestcaseCertificateUtils.class);

    public static ToolTestcaseCertificateResultType processCertificateData(byte[] certData, CertificateInfo certInfo, MailAddress mailAddr,
        Set<CertificateValidator> certValidators) {
        try {
            certInfo.setCertificate(CertificateUtils.readCertificate(certData, CertificateType.X509, DataEncoding.PEM));

            return validateCertificate(certInfo, mailAddr, certValidators);
        } catch (CryptographyException e) {
            return ToolTestcaseCertificateResultType.UNREADABLE_CERT_DATA;
        }
    }

    public static ToolTestcaseCertificateResultType
        validateCertificate(CertificateInfo certInfo, MailAddress mailAddr, Set<CertificateValidator> certValidators) {
        for (CertificateValidator validator : certValidators) {
            try {
                if (!validator.validate(mailAddr, certInfo) && !validator.isOptional()) {
                    LOGGER.info(String.format("X509 certificate for mail address (%s) is not valid (class=%s).", mailAddr, ToolClassUtils.getName(validator)));

                    return validator.getErrorCode();
                }
            } catch (Exception e) {
                LOGGER.error(String.format("Unable to validate (class=%s) X509 certificate for mail address: %s", ToolClassUtils.getName(validator), mailAddr),
                    e);

                if (!validator.isOptional()) {
                    return validator.getErrorCode();
                }
            }
        }

        return ToolTestcaseCertificateResultType.VALID_CERT;
    }
}