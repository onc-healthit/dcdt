package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig.GenerateConstraintGroup;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.GeneralNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateGenerator;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateIntervalConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.KeyUsageType;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyGenerator;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.DigestUtils;
import gov.hhs.onc.dcdt.crypto.utils.GeneralNameUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolValidationUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component("certGenImpl")
public class CertificateGeneratorImpl extends AbstractCryptographyGenerator<CertificateConfig, CertificateInfo> implements CertificateGenerator {
    private final static Logger LOGGER = LoggerFactory.getLogger(CertificateGeneratorImpl.class);

    private Map<CertificateDn, BigInteger> issuedSerialNums = new HashMap<>();
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexBuilder.append(String.format("%02X", b));
        }
        return hexBuilder.toString();
    }

    @Override
    public CertificateInfo generateCertificate(KeyInfo keyPairInfo, CertificateConfig certConfig) throws CryptographyException {
        return this.generateCertificate(null, keyPairInfo, certConfig);
    }

    @Override
    public CertificateInfo generateCertificate(@Nullable CredentialInfo issuerCredInfo, KeyInfo keyPairInfo, CertificateConfig certConfig)
        throws CryptographyException {
        BindingResult certConfigBindingResult = this.validateConfig(certConfig, GenerateConstraintGroup.class);

        if (certConfigBindingResult.hasErrors()) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format("Invalid certificate (subjDn={%s}) configuration (class=%s): %s",
                certConfig.getSubjectDn(), ToolClassUtils.getName(certConfig),
                ToolStringUtils.joinDelimit(ToolValidationUtils.mapErrorMessages(this.msgSourceValidation, certConfigBindingResult).entrySet(), ", ")));
        }

        boolean hasIssuerCredInfo = (issuerCredInfo != null);
        KeyInfo issuerKeyPairInfo = (hasIssuerCredInfo ? issuerCredInfo.getKeyDescriptor() : keyPairInfo);

        CertificateType certType = certConfig.getCertificateType();
        CertificateDescriptor<?> issuerCertDesc = (hasIssuerCredInfo ? issuerCredInfo.getCertificateDescriptor() : certConfig);
        LOGGER.info("Issuer Cert Desc: {},{},{}", issuerCertDesc.getSerialNumber(), issuerCertDesc.getSubjectDn(),issuerCertDesc.getCertificateType());
        X509v3CertificateBuilder certBuilder = this.generateCertificateBuilder(issuerKeyPairInfo, issuerCertDesc, keyPairInfo, certConfig);

        try {
            SignatureAlgorithm certSigAlg = certConfig.getSignatureAlgorithm();
            // noinspection ConstantConditions
            X509CertificateHolder certHolder =
                certBuilder.build(new BcRSAContentSignerBuilder(certSigAlg.getAlgorithmId(), certSigAlg.getDigestAlgorithm().getAlgorithmId())
                    .build(PrivateKeyFactory.createKey(issuerKeyPairInfo.getPrivateKeyInfo())));

            CertificateInfo certInfo;

            try (InputStream certInStream = new ByteArrayInputStream(certHolder.getEncoded())) {
                certInfo = new CertificateInfoImpl((X509Certificate) CertificateUtils.getCertificateFactory(certType).generateCertificate(certInStream));
            }

            // noinspection ConstantConditions
            LOGGER.info(String.format("Generated certificate (type=%s, subjDn={%s}, serialNum=%s, issuerDn={%s}).", certInfo.getCertificateType().name(),
                certInfo.getSubjectDn(), certInfo.getSerialNumber(), certInfo.getIssuerDn()));

            return certInfo;
        } catch (CertificateException | IOException | OperatorCreationException e) {
            // noinspection ConstantConditions
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format(
                "Unable to generate certificate (type=%s, subjDn={%s}, serialNum=%s, issuerDn={%s}).", certType.name(), certConfig.getSubjectDn(),
                certConfig.getSerialNumber(), issuerCertDesc.getSubjectDn()), e);
        }
    }

    private <T extends CertificateDescriptor<?>> X509v3CertificateBuilder generateCertificateBuilder(KeyInfo issuerKeyPairInfo, T issuerCertDesc,
        KeyInfo keyPairInfo, CertificateConfig certConfig) throws CryptographyException {
        boolean certCa = certConfig.isCertificateAuthority();
        CertificateIntervalConfig certIntervalConfig = certConfig.getInterval();
        // noinspection ConstantConditions
        long certIntervalNotBeforeTime = (new Date().getTime() + certIntervalConfig.getOffset());

        if (!certConfig.hasSerialNumber()) {
            CertificateDn issuerDn = issuerCertDesc.getSubjectDn();
            BigInteger certSerialNumValue;

            this.issuedSerialNums.put(
                issuerDn,
                (this.issuedSerialNums.containsKey(issuerDn)
                    ? (certSerialNumValue = BigInteger.valueOf((this.issuedSerialNums.get(issuerDn).longValue() + 1))) : (certSerialNumValue =
                        BigInteger.valueOf(1))));

            certConfig.setSerialNumber(new CertificateSerialNumberImpl(certSerialNumValue));
        }

        // noinspection ConstantConditions
        X509v3CertificateBuilder certBuilder =
            new X509v3CertificateBuilder(issuerCertDesc.getSubjectDn().toX500Name(), certConfig.getSerialNumber().getValue(), new Date(
                certIntervalNotBeforeTime), new Date((certIntervalNotBeforeTime + certIntervalConfig.getDuration())), certConfig.getSubjectDn().toX500Name(),
                keyPairInfo.getSubjectPublicKeyInfo());

        try {
            if (!certCa) {

                certBuilder.addExtension(Extension.authorityKeyIdentifier, false, keyPairInfo.getAuthorityKeyId());
                LOGGER.info("Authority Key Identifier from CertificateGeneratorImpl: {} ", bytesToHex(keyPairInfo.getAuthorityKeyId().getKeyIdentifier()));

            }

            // noinspection ConstantConditions
           certBuilder.addExtension(Extension.subjectKeyIdentifier, false, keyPairInfo.getSubjectKeyId());
            LOGGER.info("Subject Key Identifier from CertificateGeneratorImpl: {}", bytesToHex(keyPairInfo.getSubjectKeyId().getKeyIdentifier()));
            certBuilder.addExtension(Extension.basicConstraints, false, new BasicConstraints(certCa));

            if (certConfig.hasKeyUsages()) {
                int keyUsage = 0;

                // noinspection ConstantConditions
                for (KeyUsageType keyUsageType : certConfig.getKeyUsages()) {
                    keyUsage |= keyUsageType.getTag();
                }

                certBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(keyUsage));
            }

            if (certConfig.hasSubjectAltNames()) {
                certBuilder.addExtension(Extension.subjectAlternativeName, false, GeneralNameUtils.fromMap(certConfig.getSubjectAltNames()));
            }

            if (certConfig.hasCrlDistributionUris()) {
                // noinspection ConstantConditions
                certBuilder.addExtension(
                    Extension.cRLDistributionPoints,
                    false,
                    new CRLDistPoint(certConfig
                        .getCrlDistributionUris()
                        .stream()
                        .map(
                            crlDistribUri -> new DistributionPoint(new DistributionPointName(new GeneralNames(new GeneralName(
                                GeneralNameType.UNIFORM_RESOURCE_IDENTIFIER.getTag(), crlDistribUri.toString()))), null, null))
                        .toArray(DistributionPoint[]::new)));
            }

            if (certConfig.hasIssuerAccessUris()) {
                ASN1EncodableVector certIssuerAccessDescVector = new ASN1EncodableVector();

                // noinspection ConstantConditions
                certConfig
                    .getIssuerAccessUris()
                    .stream()
                    .forEach(
                        certIssuerAccessUri -> certIssuerAccessDescVector.add(new AccessDescription(AccessDescription.id_ad_caIssuers, new GeneralName(
                            GeneralNameType.UNIFORM_RESOURCE_IDENTIFIER.getTag(), certIssuerAccessUri.toString()))));

                certBuilder.addExtension(Extension.authorityInfoAccess, false,
                    AuthorityInformationAccess.getInstance(new DERSequence(certIssuerAccessDescVector)));
            }
        } catch (CertIOException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException("Unable to set certificate X509v3 extension.", e);
        }

        return certBuilder;
    }
}
