package gov.hhs.onc.dcdt.crypto.crl.impl;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig.GenerateConstraintGroup;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.crl.CrlConfig;
import gov.hhs.onc.dcdt.crypto.crl.CrlEntryConfig;
import gov.hhs.onc.dcdt.crypto.crl.CrlException;
import gov.hhs.onc.dcdt.crypto.crl.CrlGenerator;
import gov.hhs.onc.dcdt.crypto.crl.CrlInfo;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyGenerator;
import gov.hhs.onc.dcdt.crypto.utils.CrlUtils.ToolX509Crl;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolValidationUtils;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CRLException;
import java.util.Date;
import javax.annotation.Resource;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component("crlGenImpl")
public class CrlGeneratorImpl extends AbstractCryptographyGenerator<CrlConfig, CrlInfo> implements CrlGenerator {
    private final static Logger LOGGER = LoggerFactory.getLogger(CrlGeneratorImpl.class);

    @Resource(name = "secureRandomSha1")
    private SecureRandom secureRandom;

    @Override
    public CrlInfo generateCrl(PrivateKeyInfo issuerPrivateKeyInfo, AuthorityKeyIdentifier issuerAuthKeyId, CrlConfig crlConfig) throws CrlException {
        BindingResult crlConfigBindingResult = this.validateConfig(crlConfig, GenerateConstraintGroup.class);

        if (crlConfigBindingResult.hasErrors()) {
            throw new CrlException(String.format("Invalid CRL configuration (class=%s): %s", ToolClassUtils.getName(crlConfig),
                ToolStringUtils.joinDelimit(ToolValidationUtils.mapErrorMessages(this.msgSourceValidation, crlConfigBindingResult).entrySet(), ", ")));
        }

        CertificateDn issuerDn = crlConfig.getIssuerDn();

        try {
            Date updateDate = new Date();
            // noinspection ConstantConditions
            X509v2CRLBuilder builder = new X509v2CRLBuilder(issuerDn.toX500Name(), updateDate);
            builder.setNextUpdate(new Date(updateDate.getTime() + 1));
            builder.addExtension(Extension.authorityKeyIdentifier, false, issuerAuthKeyId);
            builder.addExtension(Extension.cRLNumber, false, new CRLNumber(crlConfig.getNumber()));

            for (CrlEntryConfig crlEntryConfig : crlConfig.getEntries().values()) {
                // noinspection ConstantConditions
                builder.addCRLEntry(crlEntryConfig.getSerialNumber(), crlEntryConfig.getRevocationDate(), crlEntryConfig.getRevocationReason().getTag());
            }

            SignatureAlgorithm sigAlg = crlConfig.getSignatureAlgorithm();

            // noinspection ConstantConditions
            CrlInfo crlInfo =
                new CrlInfoImpl(new ToolX509Crl(builder.build(
                    new BcRSAContentSignerBuilder(sigAlg.getAlgorithmId(), sigAlg.getDigestAlgorithm().getAlgorithmId()).setSecureRandom(this.secureRandom)
                        .build(PrivateKeyFactory.createKey(issuerPrivateKeyInfo))).toASN1Structure()));

            LOGGER.info(String.format("Generated CRL (issuerDn={%s}, sigAlg=%s).", issuerDn, sigAlg.name()));

            return crlInfo;
        } catch (CRLException | IOException | OperatorCreationException e) {
            throw new CrlException(String.format("Unable to generate CRL (issuerDn={%s}).", issuerDn), e);
        }
    }
}
