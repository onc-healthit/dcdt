package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.impl.ToolMessageImpl;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateRevocationStatus;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidatorContext;
import gov.hhs.onc.dcdt.crypto.crl.CrlEntryInfo;
import gov.hhs.onc.dcdt.crypto.crl.CrlException;
import gov.hhs.onc.dcdt.crypto.crl.CrlInfo;
import gov.hhs.onc.dcdt.crypto.crl.CrlType;
import gov.hhs.onc.dcdt.crypto.crl.impl.CrlInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.CrlUtils;
import gov.hhs.onc.dcdt.crypto.utils.CrlUtils.ToolX509Crl;
import gov.hhs.onc.dcdt.http.lookup.HttpLookupResult;
import gov.hhs.onc.dcdt.http.lookup.HttpLookupService;
import gov.hhs.onc.dcdt.http.utils.ToolHttpUtils;
import java.math.BigInteger;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class CertificateRevocationStatusConstraintValidator extends AbstractCertificateConstraintValidator<CertificateRevocationStatus> {
    private final static DateFormat CERT_CRL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    @Resource(name = "httpLookupServiceCombined")
    private HttpLookupService httpLookupService;

    @Override
    protected boolean isValidInternal(CertificateValidatorContext certValidatorContext, ConstraintValidatorContext validatorContext) throws Exception {
        CertificateInfo certInfo = certValidatorContext.getCertificateInfo();
        CertificateDn certSubjDn = certInfo.getSubjectDn(), certIssuerDn = certInfo.getIssuerDn();

        if (!certInfo.hasCrlDistributionUris()) {
            certValidatorContext.getMessages().add(
                new ToolMessageImpl(ToolMessageLevel.WARN, String.format(
                    "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) does not contain a cRLDistributionPoints X509v3 extension.", certSubjDn,
                    certInfo.getSerialNumber(), certIssuerDn)));

            return true;
        }

        // noinspection ConstantConditions
        URI[] certCrlDistribUris =
            certInfo.getCrlDistributionUris().stream()
                .filter(certCrlDistribUri -> StringUtils.equalsIgnoreCase(certCrlDistribUri.getScheme(), ToolHttpUtils.HTTP_SCHEME)).toArray(URI[]::new);
        List<CrlInfo> certCrlInfos = new ArrayList<>(certCrlDistribUris.length);
        HttpLookupResult certCrlLookupResult;

        for (URI certCrlDistribUri : certCrlDistribUris) {
            if (!(certCrlLookupResult = this.httpLookupService.getUri(certCrlDistribUri)).isSuccess()) {
                // noinspection ConstantConditions
                certValidatorContext
                    .getMessages()
                    .add(
                        new ToolMessageImpl(
                            ToolMessageLevel.WARN,
                            String
                                .format(
                                    "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) CRL distribution point HTTP URI (%s) lookup response (status=%d, headers=[%s]) was not successful: [%s]",
                                    certSubjDn, certInfo.getSerialNumber(), certIssuerDn, certCrlDistribUri, certCrlLookupResult.getResponseStatus().code(),
                                    StringUtils.join(certCrlLookupResult.getResponseHeaders(), "; "), StringUtils.join(certCrlLookupResult.getMessages(), "; "))));

                continue;
            }

            if (!certCrlLookupResult.hasResponseContent()) {
                // noinspection ConstantConditions
                certValidatorContext
                    .getMessages()
                    .add(
                        new ToolMessageImpl(
                            ToolMessageLevel.WARN,
                            String
                                .format(
                                    "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) CRL distribution point HTTP URI (%s) lookup response (status=%d, headers=[%s]) contains no data.",
                                    certSubjDn, certInfo.getSerialNumber(), certIssuerDn, certCrlDistribUri, certCrlLookupResult.getResponseStatus().code(),
                                    StringUtils.join(certCrlLookupResult.getResponseHeaders(), "; "))));

                continue;
            }

            List<ToolX509Crl> certCrls;

            try {
                certCrls = CrlUtils.readCrls(certCrlLookupResult.getResponseContent(), CrlType.X509);
            } catch (CryptographyException e) {
                // noinspection ConstantConditions
                throw new CrlException(
                    String.format(
                        "Unable to read certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) CRL distribution point HTTP URI (%s) lookup response (status=%d, headers=[%s]) CRL instance(s): %s",
                        certSubjDn, certInfo.getSerialNumber(), certIssuerDn, certCrlDistribUri, certCrlLookupResult.getResponseStatus().code(),
                        StringUtils.join(certCrlLookupResult.getResponseHeaders(), "; "), e.getMessage()), e);
            }

            CrlInfo certCrlInfo;
            CertificateDn certCrlIssuerDn;

            for (ToolX509Crl certCrl : certCrls) {
                try {
                    certCrlInfos.add((certCrlInfo = new CrlInfoImpl(certCrl)));
                } catch (CrlException e) {
                    // noinspection ConstantConditions
                    throw new CrlException(
                        String.format(
                            "Unable to wrap certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) CRL distribution point HTTP URI (%s) lookup response (status=%d, headers=[%s]) CRL instance (issuerDn={%s}): %s",
                            certSubjDn, certInfo.getSerialNumber(), certIssuerDn, certCrlDistribUri, certCrlLookupResult.getResponseStatus().code(),
                            StringUtils.join(certCrlLookupResult.getResponseHeaders(), "; "), certCrl.getIssuerX500Principal().getName(), e.getMessage()), e);
                }

                // noinspection ConstantConditions
                if (!certIssuerDn.equals((certCrlIssuerDn = certCrlInfo.getIssuerDn()))) {
                    // noinspection ConstantConditions
                    throw new CrlException(
                        String
                            .format(
                                "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) CRL distribution point HTTP URI (%s) lookup response (status=%d, headers=[%s]) CRL instance issuer Distinguished Name (DN) does not match: %s != %s",
                                certSubjDn, certInfo.getSerialNumber(), certIssuerDn, certCrlDistribUri, certCrlLookupResult.getResponseStatus().code(),
                                StringUtils.join(certCrlLookupResult.getResponseHeaders(), "; "), certCrlIssuerDn, certIssuerDn));
                }
            }
        }

        // noinspection ConstantConditions
        BigInteger certSerialNum = certInfo.getSerialNumber().getValue();
        Map<BigInteger, CrlEntryInfo> certCrlEntryInfos;

        for (CrlInfo certCrlInfo : certCrlInfos) {
            if (!(certCrlEntryInfos = certCrlInfo.getEntries()).containsKey(certSerialNum)) {
                continue;
            }

            CrlEntryInfo certCrlEntryInfo = certCrlEntryInfos.get(certSerialNum);

            // noinspection ConstantConditions
            throw new CrlException(
                String
                    .format(
                        "Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) CRL instance (thisUpdate={%s}, nextUpdate={%s}) entry is revoked (reason=%s, date={%s}).",
                        certSubjDn, certInfo.getSerialNumber(), certIssuerDn, CERT_CRL_DATE_FORMAT.format(certCrlInfo.getThisUpdate()), (certCrlInfo
                            .hasNextUpdate() ? CERT_CRL_DATE_FORMAT.format(certCrlInfo.getNextUpdate()) : null), certCrlEntryInfo.getRevocationReason().name(),
                        (certCrlEntryInfo.hasRevocationDate() ? CERT_CRL_DATE_FORMAT.format(certCrlEntryInfo.getRevocationDate()) : null)));
        }

        // noinspection ConstantConditions
        certValidatorContext.getMessages().add(
            new ToolMessageImpl(ToolMessageLevel.INFO, String.format("Certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) is not revoked.", certSubjDn,
                certInfo.getSerialNumber(), certIssuerDn)));

        return true;
    }
}
