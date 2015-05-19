package gov.hhs.onc.dcdt.discovery.steps.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoValidator;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.discovery.steps.CertificateLookupStep;
import gov.hhs.onc.dcdt.discovery.steps.CertificateValidationStep;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.springframework.beans.factory.annotation.Autowired;

public class CertificateValidationStepImpl extends AbstractCertificateDiscoveryStep implements CertificateValidationStep {
    @Autowired
    private CertificateInfoValidator certInfoValidator;

    private CertificateInfo validCertInfo;
    private List<CertificateInfo> invalidCertInfos;

    public CertificateValidationStepImpl() {
        super(BindingType.NONE);
    }

    @Override
    public boolean execute(List<CertificateDiscoveryStep> prevSteps, MailAddress directAddr) {
        for (CertificateLookupStep<?, ?, ?, ?> certLookupStep : CollectionUtils.collect(prevSteps, new ToolCollectionUtils.AssignableTransformer<>(
            CertificateLookupStep.class))) {
            if (certLookupStep != null && certLookupStep.isSuccess() && certLookupStep.hasCertificateInfos()) {
                Pair<Boolean, List<String>> certInfoValidationResultPair;
                this.invalidCertInfos = new ArrayList<>();

                // noinspection ConstantConditions
                for (CertificateInfo certInfo : certLookupStep.getCertificateInfos()) {
                    this.execMsgs.addAll((certInfoValidationResultPair = this.certInfoValidator.validate(directAddr, certInfo)).getRight());

                    if (certInfoValidationResultPair.getLeft()) {
                        try {
                            X509Certificate cert = certInfo.getCertificate();
                            CertificateName certSubjName = certInfo.getSubjectName();
                            // noinspection ConstantConditions
                            GeneralNames altNames = certInfo.getSubjectName().getAltNames();

                            if (altNames != null && altNames.getNames().length > 1) {
                                // noinspection ConstantConditions
                                this.execMsgs.add(String.format(
                                    "Certificate (subj={%s}, serialNum=%s, issuer={%s}) subjectAltName X509v3 extension contains multiple rfc822 and/or dNSName values.",
                                    certSubjName, certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName()));
                            }
                        } catch (CertificateException e) {
                        }
                    }

                    if (certInfoValidationResultPair.getLeft()) {
                        this.validCertInfo = certInfo;
                    } else {
                        this.invalidCertInfos.add(certInfo);
                    }
                }
            }
        }

        return this.isSuccess();
    }

    @Override
    public boolean isSuccess() {
        return (super.isSuccess() && this.hasValidCertificateInfo());
    }

    @Override
    public boolean hasValidCertificateInfo() {
        return (this.validCertInfo != null);
    }

    @Nullable
    @Override
    public CertificateInfo getValidCertificateInfo() {
        return this.validCertInfo;
    }

    @Override
    public boolean hasInvalidCertificateInfos() {
        return !CollectionUtils.isEmpty(this.invalidCertInfos);
    }

    @Nullable
    @Override
    public List<CertificateInfo> getInvalidCertificateInfos() {
        return this.invalidCertInfos;
    }
}
