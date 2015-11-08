package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.PemType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils.ToolProviderJcaJceHelper;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;

public abstract class CertificateUtils {
    public final static JcaX509CertificateConverter CERT_CONV = new JcaX509CertificateConverter() {
        {
            this.setProvider(CryptographyUtils.PROVIDER);
        }
    };

    public static List<X509Certificate> readCertificates(byte[] data, CertificateType certType) throws CryptographyException {
        return readCertificates(CryptographyUtils.PROVIDER_HELPER, data, certType);
    }

    public static List<X509Certificate> readCertificates(ToolProviderJcaJceHelper provHelper, byte[] data, CertificateType certType)
        throws CryptographyException {
        try (InputStream certsInStream = new ByteArrayInputStream(data)) {
            return ToolStreamUtils.asInstances(getCertificateFactory(provHelper, certType).generateCertificates(certsInStream).stream(), X509Certificate.class)
                .collect(Collectors.toList());
        } catch (CertificateException | IOException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format(
                "Unable to read certificate instance(s) of type (id=%s, providerName=%s) from data.", certType.getId(), provHelper.getProvider().getName()), e);
        }
    }

    public static X509Certificate readCertificate(byte[] data, CertificateType certType, DataEncoding dataEnc) throws CryptographyException {
        return readCertificate(CryptographyUtils.PROVIDER_HELPER, data, certType, dataEnc);
    }

    public static X509Certificate readCertificate(ToolProviderJcaJceHelper provHelper, byte[] data, CertificateType certType, DataEncoding dataEnc)
        throws CryptographyException {
        try {
            if (dataEnc == DataEncoding.PEM) {
                data = PemUtils.writePemContent(CryptographyUtils.findByType(PemType.class, certType.getType()), data);
            }

            try (InputStream certInStream = new ByteArrayInputStream(data)) {
                return ((X509Certificate) getCertificateFactory(provHelper, certType).generateCertificate(certInStream));
            }
        } catch (CertificateException | IOException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format(
                "Unable to read certificate instance of type (id=%s, providerName=%s) from data.", certType.getId(), provHelper.getProvider().getName()), e);
        }
    }

    public static byte[] writeCertificate(X509Certificate cert, DataEncoding dataEnc) throws CryptographyException {
        try {
            byte[] data = cert.getEncoded();

            return ((dataEnc == DataEncoding.DER) ? data : PemUtils.writePemContent(PemType.CERTIFICATE, data));
        } catch (CertificateEncodingException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format("Unable to write certificate instance (class=%s) to data.",
                ToolClassUtils.getClass(cert)), e);
        }
    }

    public static CertificateFactory getCertificateFactory(CertificateType certType) throws CryptographyException {
        return getCertificateFactory(CryptographyUtils.PROVIDER_HELPER, certType);
    }

    public static CertificateFactory getCertificateFactory(ToolProviderJcaJceHelper provHelper, CertificateType certType) throws CryptographyException {
        try {
            return provHelper.createCertificateFactory(certType.getId());
        } catch (CertificateException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format(
                "Unable to get certificate factory instance for certificate type (id=%s, providerName=%s).", certType.getId(), provHelper.getProvider()
                    .getName()), e);
        }
    }
}
