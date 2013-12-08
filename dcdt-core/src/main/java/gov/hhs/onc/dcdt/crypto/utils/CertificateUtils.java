package gov.hhs.onc.dcdt.crypto.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import gov.hhs.onc.dcdt.crypto.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.CertificateName;
import gov.hhs.onc.dcdt.crypto.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.constants.CertificateType;
import gov.hhs.onc.dcdt.crypto.constants.DataEncoding;
import gov.hhs.onc.dcdt.crypto.constants.PemType;

public abstract class CertificateUtils {
    private final static int SERIAL_NUM_SEED_SIZE = 8;

    public static X509Certificate generateCertificate(CredentialInfo credentialInfo) throws CryptographyException {
        X509Certificate certificate;
        CertificateInfo certificateInfo = credentialInfo.getCertificateInfo();

        PrivateKey signerKey;
        if (!certificateInfo.getIsCa()) {
            signerKey = certificateInfo.getIssuerPrivateKey();
        } else {
            signerKey = credentialInfo.getKeyPairInfo().getPrivateKey();
        }

        try {
            AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find(certificateInfo.getSigAlgName());
            AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
            AsymmetricKeyParameter keyParam = PrivateKeyFactory.createKey(signerKey.getEncoded());

            X509v3CertificateBuilder certGen = generateCertBuilder(certificateInfo);
            X509CertificateHolder certHolder = certGen.build(new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(keyParam));

            certificate = (X509Certificate) CertificateFactory.getInstance(CertificateType.X509.getType(), BouncyCastleProvider.PROVIDER_NAME)
                .generateCertificate(new ByteArrayInputStream(certHolder.getEncoded()));
        } catch (CertificateException | OperatorCreationException | IOException | NoSuchProviderException e) {
            throw new CryptographyException("Cannot generate certificate", e);
        }

        return certificate;
    }

    private static X509v3CertificateBuilder generateCertBuilder(CertificateInfo certificateInfo) throws CertIOException {
        CertificateName issuer = certificateInfo.getIssuer();

        X509v3CertificateBuilder certGen = new X509v3CertificateBuilder(issuer.toX500Name(), certificateInfo.getSerialNumber(), certificateInfo
            .getValidInterval().getNotBefore(), certificateInfo.getValidInterval().getNotAfter(), certificateInfo.getSubject().toX500Name(),
            certificateInfo.getPublicKey());

        certGen.addExtension(X509Extension.basicConstraints, false, new BasicConstraints(certificateInfo.getIsCa()));

        GeneralNames subjAltNames = certificateInfo.getSubject().getSubjAltNames();

        if (!certificateInfo.getIsCa()) {
            certGen.addExtension(X509Extension.authorityKeyIdentifier, false, certificateInfo.getAuthKeyId());
            certGen.addExtension(X509Extension.subjectKeyIdentifier, false, certificateInfo.getSubjKeyId());

            GeneralNames issuerAltNames = issuer.getSubjAltNames();

            if (issuerAltNames != null) {
                certGen.addExtension(X509Extension.issuerAlternativeName, false, issuerAltNames);
            }
        }

        if (subjAltNames != null) {
            certGen.addExtension(X509Extension.subjectAlternativeName, false, subjAltNames);
        }
        return certGen;
    }

    public static X509Certificate readCertificate(InputStream stream, DataEncoding encoding) throws CryptographyException {
        try {
            return readCertificate(IOUtils.toByteArray(stream), encoding);
        } catch (IOException e) {
            throw new CryptographyException("Unable to read X509 certificate from stream: encoding=" + encoding.getEncoding(), e);
        }
    }

    public static X509Certificate readCertificate(File file, DataEncoding encoding) throws CryptographyException {
        try {
            return readCertificate(new FileInputStream(file), encoding);
        } catch (IOException e) {
            throw new CryptographyException("Unable to read X509 certificate from file (" + file + "): encoding=" + encoding.getEncoding(), e);
        }
    }

    @SuppressWarnings({ "fallthrough" })
    public static X509Certificate readCertificate(byte[] data, DataEncoding encoding) throws CryptographyException {
        try {
            switch (encoding) {
                case PEM:
                    data = CryptographyUtils.readPemContent(new ByteArrayInputStream(data));

                case DER:
                    Certificate cert = getX509CertFactory().generateCertificate(new ByteArrayInputStream(data));

                    if (!(cert instanceof X509Certificate)) {
                        throw new CryptographyException("Certificate (type=" + cert.getType() + ") is not a X509 certificate: " + cert);
                    }

                    return (X509Certificate) cert;
                default:
                    throw new CryptographyException("Unknown X509 certificate data encoding: " + encoding.getEncoding());
            }
        } catch (CertificateException e) {
            throw new CryptographyException("Unable to read X509 certificate from data (length=" + ArrayUtils.getLength(data) + "): encoding="
                + encoding.getEncoding(), e);
        }
    }

    public static void writeCertificate(File file, X509Certificate cert, DataEncoding encoding) throws CryptographyException {
        try {
            writeCertificate(new FileOutputStream(file), cert, encoding);
        } catch (IOException e) {
            throw new CryptographyException("Unable to write X509 certificate to file (" + file + "): encoding=" + encoding.getEncoding(), e);
        }
    }

    public static void writeCertificate(OutputStream stream, X509Certificate cert, DataEncoding encoding) throws CryptographyException {
        try {
            byte[] data = cert.getEncoded();

            switch (encoding) {
                case PEM:
                    CryptographyUtils.writePemContent(stream, PemType.X509_CERTIFICATE.getType(), data);
                    break;

                case DER:
                    IOUtils.write(data, stream);
                    break;

                default:
                    throw new CryptographyException("Unknown X509 certificate data encoding: " + encoding.getEncoding());
            }
        } catch (CertificateEncodingException | IOException e) {
            throw new CryptographyException("Unable to write X509 certificate to stream: encoding=" + encoding.getEncoding(), e);
        }
    }

    public static CertificateFactory getX509CertFactory() throws CryptographyException {
        try {
            return CertificateFactory.getInstance(CertificateType.X509.getType(), CryptographyUtils.BOUNCY_CASTLE_PROVIDER);
        } catch (CertificateException e) {
            throw new CryptographyException("Unable to get X509 certificate factory (type=" + CertificateType.X509.getType()
                + ") instance from BouncyCastle provider (name= " + CryptographyUtils.BOUNCY_CASTLE_PROVIDER.getName() + " ).", e);
        }
    }

    public static BigInteger generateSerialNum(Set<BigInteger> existingSerialNums) {
        SecureRandom rand = CryptographyUtils.getRandom(SERIAL_NUM_SEED_SIZE);
        BigInteger serialNum;

        do {
            serialNum = BigInteger.valueOf(rand.nextLong()).abs();
        } while (existingSerialNums != null && existingSerialNums.contains(serialNum));

        if (existingSerialNums != null) {
            existingSerialNums.add(serialNum);
        }

        return serialNum;
    }
}
