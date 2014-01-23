package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig.GenerateConstraintGroup;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateGenerator;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidInterval;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyGenerator;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolValidationUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component("certGenImpl")
@Scope("singleton")
public class CertificateGeneratorImpl extends AbstractCryptographyGenerator<CertificateConfig, CertificateInfo> implements CertificateGenerator {
    @Override
    public CertificateInfo generateCertificate(KeyInfo keyPairInfo, CertificateConfig certConfig) throws CryptographyException {
        return this.generateCertificate(null, keyPairInfo, certConfig);
    }

    @Override
    public CertificateInfo generateCertificate(@Nullable CredentialInfo issuerCredInfo, KeyInfo keyPairInfo, CertificateConfig certConfig)
        throws CryptographyException {
        BindingResult certConfigBindingResult = this.validateConfig(certConfig, GenerateConstraintGroup.class);

        if (certConfigBindingResult.hasErrors()) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException(String.format("Invalid certificate configuration (class=%s): %s",
                ToolClassUtils.getName(certConfig),
                ToolStringUtils.joinDelimit(ToolValidationUtils.mapErrorMessages(this.msgSourceValidation, certConfigBindingResult).entrySet(), ", ")));
        }

        boolean hasIssuerCredInfo = (issuerCredInfo != null);
        CertificateType certType = certConfig.getCertificateType();
        X509v3CertificateBuilder certBuilder =
            this.generateCertificateBuilder((hasIssuerCredInfo ? issuerCredInfo.getKeyDescriptor() : keyPairInfo),
                (hasIssuerCredInfo ? issuerCredInfo.getCertificateDescriptor() : certConfig), keyPairInfo, certConfig);

        try {
            SignatureAlgorithm certSigAlg = certConfig.getSignatureAlgorithm();
            X509CertificateHolder certHolder =
                certBuilder.build(new BcRSAContentSignerBuilder(certSigAlg.getId(), certSigAlg.getDigestId()).build(PrivateKeyFactory.createKey(certConfig
                    .isCertificateAuthority() ? keyPairInfo.getPrivateKeyInfo() : issuerCredInfo.getKeyDescriptor().getPrivateKeyInfo())));

            return new CertificateInfoImpl((X509Certificate) CertificateUtils.getCertificateFactory(certType).generateCertificate(
                new ByteArrayInputStream(certHolder.getEncoded())));
        } catch (CertificateException | IOException | OperatorCreationException e) {
            throw new gov.hhs.onc.dcdt.crypto.certs.CertificateException("Unable to generate certificate.", e);
        }
    }

    private <T extends CertificateDescriptor> X509v3CertificateBuilder generateCertificateBuilder(KeyInfo issuerKeyPairInfo, T issuerCertDesc,
        KeyInfo keyPairInfo, CertificateConfig certConfig) throws CryptographyException {
        boolean certCa = certConfig.isCertificateAuthority();
        CertificateName certSubj = certConfig.getSubject();
        CertificateValidInterval certValidInterval = certConfig.getValidInterval();

        X509v3CertificateBuilder certBuilder =
            new X509v3CertificateBuilder((certCa ? certConfig : issuerCertDesc).getSubject().toX500Name(), (certConfig.hasSerialNumber()
                ? certConfig.getSerialNumber() : CertificateUtils.generateSerialNumber()), certValidInterval.getNotBefore(), certValidInterval.getNotAfter(),
                certSubj.toX500Name(), keyPairInfo.getSubjectPublicKeyInfo());

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
