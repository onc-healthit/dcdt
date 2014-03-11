package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.PemType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public abstract class CertificateUtils {
    private final static int SERIAL_NUM_GEN_RAND_SEED_SIZE_DEFAULT = 8;

    public static BigInteger generateSerialNumber() throws CryptographyException {
        return BigInteger.valueOf(SecureRandomUtils.getRandom(SERIAL_NUM_GEN_RAND_SEED_SIZE_DEFAULT).nextLong()).abs();
    }

    public static X509Certificate readCertificate(InputStream inStream, CertificateType certType, DataEncoding dataEnc) throws CryptographyException {
        return readCertificate(new InputStreamReader(inStream), certType, dataEnc);
    }

    public static X509Certificate readCertificate(Reader reader, CertificateType certType, DataEncoding dataEnc) throws CryptographyException {
        try {
            return readCertificate(IOUtils.toByteArray(reader), certType, dataEnc);
        } catch (IOException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format(
                "Unable to read certificate instance of type (name=%s, providerName=%s) from reader (class=%s).", certType.getName(),
                CryptographyUtils.PROVIDER_NAME, ToolClassUtils.getName(reader)), e);
        }
    }

    public static X509Certificate readCertificate(byte[] data, CertificateType certType, DataEncoding dataEnc) throws CryptographyException {
        try {
            if (dataEnc == DataEncoding.PEM) {
                data = PemUtils.writePemContent(certType.getName(), data);
            }

            return (X509Certificate) getCertificateFactory(certType).generateCertificate(new ByteArrayInputStream(data));
        } catch (CertificateException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format(
                "Unable to read certificate instance of type (name=%s, providerName=%s) from data.", certType.getName(), CryptographyUtils.PROVIDER_NAME), e);
        }
    }

    public static void writeCertificate(OutputStream outStream, X509Certificate cert, DataEncoding dataEnc) throws CryptographyException {
        writeCertificate(new OutputStreamWriter(outStream), cert, dataEnc);
    }

    public static void writeCertificate(Writer writer, X509Certificate cert, DataEncoding dataEnc) throws CryptographyException {
        try {
            IOUtils.write(writeCertificate(cert, dataEnc), writer);

            writer.flush();
        } catch (IOException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format("Unable to write certificate instance (class=%s) to writer (class=%s).",
                ToolClassUtils.getClass(cert), ToolClassUtils.getName(writer)), e);
        }
    }

    public static byte[] writeCertificate(X509Certificate cert, DataEncoding dataEnc) throws CryptographyException {
        try {
            byte[] data = cert.getEncoded();

            return ((dataEnc == DataEncoding.DER) ? data : PemUtils.writePemContent(PemType.X509_CERTIFICATE, data));
        } catch (CertificateEncodingException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format("Unable to write certificate instance (class=%s) to data.",
                ToolClassUtils.getClass(cert)), e);
        }
    }

    @Nullable
    public static GeneralNames buildSubjectAltNames(@Nullable MailAddress mailAddr) {
        if (mailAddr == null) {
            return null;
        }

        GeneralName mailSubjAltName = new GeneralName(GeneralName.rfc822Name, mailAddr.toAddress());

        return mailAddr.hasLocalPart() ? new GeneralNames(mailSubjAltName) : new GeneralNames(ArrayUtils.toArray(mailSubjAltName, new GeneralName(
            GeneralName.dNSName, mailAddr.getDomainNamePart())));
    }

    public static CertificateFactory getCertificateFactory(CertificateType certType) throws CryptographyException {
        try {
            return CryptographyUtils.PROVIDER_HELPER.createCertificateFactory(certType.getName());
        } catch (CertificateException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format(
                "Unable to get certificate factory instance for certificate type (name=%s, providerName=%s).", certType.getName(),
                CryptographyUtils.PROVIDER_NAME), e);
        }
    }

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
