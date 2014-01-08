package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.PemType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidInterval;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyException;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairInfo;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolMailAddressUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;

public abstract class CertificateUtils {
    private final static int SERIAL_NUM_GEN_RAND_SEED_SIZE_DEFAULT = 8;

    public static CertificateInfo generateCertificate(KeyPairInfo keyPairInfo, CertificateConfig certConfig) throws CryptographyException {
        return generateCertificate(null, keyPairInfo, certConfig);
    }

    public static CertificateInfo generateCertificate(@Nullable CredentialInfo issuerCredInfo, KeyPairInfo keyPairInfo, CertificateConfig certConfig)
        throws CryptographyException {
        boolean hasIssuerCredInfo = (issuerCredInfo != null);
        CertificateType certType = certConfig.getCertificateType();
        X509v3CertificateBuilder certBuilder = generateCertificateBuilder((hasIssuerCredInfo ? issuerCredInfo.getKeyPairDescriptor() : keyPairInfo),
            (hasIssuerCredInfo ? issuerCredInfo.getCertificateDescriptor() : certConfig), keyPairInfo, certConfig);

        try {
            SignatureAlgorithm certSigAlg = certConfig.getSignatureAlgorithm();
            X509CertificateHolder certHolder = certBuilder.build(new BcRSAContentSignerBuilder(certSigAlg.getId(), certSigAlg.getDigestId())
                .build(PrivateKeyFactory.createKey(certConfig.isCertificateAuthority() ? keyPairInfo.getPrivateKeyInfo() : issuerCredInfo
                    .getKeyPairDescriptor().getPrivateKeyInfo())));

            return new CertificateInfoImpl((X509Certificate) getCertificateFactory(certType).generateCertificate(
                new ByteArrayInputStream(certHolder.getEncoded())));
        } catch (CertificateException | IOException | OperatorCreationException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException("Unable to generate certificate.", e);
        }
    }

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
                data = PemUtils.readPemContent(data);
            }

            return (X509Certificate) getCertificateFactory(certType).generateCertificate(new ByteArrayInputStream(data));
        } catch (CertificateException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format(
                "Unable to read certificate instance of type (name=%s, providerName=%s) from data.", certType.getName(), CryptographyUtils.PROVIDER_NAME), e);
        }
    }

    public static byte[] writeCertificate(X509Certificate cert, DataEncoding dataEnc) throws CryptographyException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        writeCertificate(outStream, cert, dataEnc);

        return outStream.toByteArray();
    }

    public static void writeCertificate(OutputStream outStream, X509Certificate cert, DataEncoding dataEnc) throws CryptographyException {
        writeCertificate(new OutputStreamWriter(outStream), cert, dataEnc);
    }

    public static void writeCertificate(Writer writer, X509Certificate cert, DataEncoding dataEnc) throws CryptographyException {
        try {
            byte[] data = cert.getEncoded();

            if (dataEnc == DataEncoding.PEM) {
                PemUtils.writePemContent(writer, PemType.X509_CERTIFICATE, data);
            } else {
                IOUtils.write(data, writer);
            }
        } catch (CertificateEncodingException | IOException e) {
            throw new KeyException(String.format("Unable to write certificate instance (class=%s) to writer (class=%s).", ToolClassUtils.getClass(cert),
                ToolClassUtils.getName(writer)), e);
        }
    }

    @Nullable
    public static GeneralNames buildSubjectAltNames(@Nullable String mailAddr) {
        if (mailAddr == null) {
            return null;
        }

        GeneralName mailSubjAltName = new GeneralName(GeneralName.rfc822Name, mailAddr);

        return ToolMailAddressUtils.hasLocal(mailAddr) ? new GeneralNames(mailSubjAltName) : new GeneralNames(ArrayUtils.toArray(mailSubjAltName,
            new GeneralName(GeneralName.dNSName, mailAddr)));
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

    private static <T extends CertificateDescriptor> X509v3CertificateBuilder generateCertificateBuilder(KeyPairInfo issuerKeyPairInfo, T issuerCertDesc,
        KeyPairInfo keyPairInfo, CertificateConfig certConfig) throws CryptographyException {
        boolean certCa = certConfig.isCertificateAuthority();
        CertificateName certSubj = certConfig.getSubject();
        CertificateValidInterval certValidInterval = certConfig.getValidInterval();

        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder((certCa ? certConfig : issuerCertDesc).getSubject().toX500Name(),
            (certConfig.hasSerialNumber() ? certConfig.getSerialNumber() : generateSerialNumber()), certValidInterval.getNotBefore(),
            certValidInterval.getNotAfter(), certSubj.toX500Name(), keyPairInfo.getSubjectPublicKeyInfo());

        try {
            certBuilder.addExtension(Extension.basicConstraints, false, new BasicConstraints(certCa));

            if (!certCa) {
                certBuilder.addExtension(Extension.authorityKeyIdentifier, false, issuerKeyPairInfo.getAuthorityKeyId());
                certBuilder.addExtension(Extension.subjectKeyIdentifier, false, keyPairInfo.getSubjectKeyId());
            }

            if (certSubj.hasSubjectAltNames()) {
                certBuilder.addExtension(Extension.subjectAlternativeName, false, certSubj.getSubjectAltNames());
            }
        } catch (CertIOException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException("Unable to set certificate X509v3 extension.", e);
        }

        return certBuilder;
    }
}
