package gov.hhs.onc.dcdt.testcases.results;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.Set;

public abstract class ToolTestcaseCertificateUtils {
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
            if (!validator.validate(mailAddr, certInfo) && !validator.isOptional()) {
                return validator.getErrorCode();
            }
        }

        return ToolTestcaseCertificateResultType.VALID_CERT;
    }
}