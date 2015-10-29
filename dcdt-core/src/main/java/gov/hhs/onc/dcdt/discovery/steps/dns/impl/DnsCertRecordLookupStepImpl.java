package gov.hhs.onc.dcdt.discovery.steps.dns.impl;

import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.impl.ToolMessageImpl;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.discovery.steps.dns.DnsCertRecordLookupStep;
import gov.hhs.onc.dcdt.dns.DnsCertificateType;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.xbill.DNS.CERTRecord;

public class DnsCertRecordLookupStepImpl extends AbstractDnsLookupStep<CERTRecord> implements DnsCertRecordLookupStep {
    private List<CertificateInfo> certInfos;

    public DnsCertRecordLookupStepImpl(BindingType bindingType, DnsLookupService lookupService) {
        super(bindingType, lookupService, DnsRecordType.CERT, CERTRecord.class);
    }

    @Override
    public boolean execute(List<CertificateDiscoveryStep> prevSteps, MailAddress directAddr) {
        if (super.execute(prevSteps, directAddr) && this.result.hasAnswers()) {
            // noinspection ConstantConditions
            List<CERTRecord> certRecords =
                this.result.getAnswers().stream().filter(certRecord -> (certRecord.getCertType() == DnsCertificateType.PKIX.getCode()))
                    .collect(Collectors.toList());

            if (certRecords.isEmpty()) {
                return this.isSuccess();
            }

            // noinspection ConstantConditions
            this.certInfos = new ArrayList<>(certRecords.size());
            CertificateInfo certInfo;

            for (CERTRecord certRecord : certRecords) {
                try {
                    this.certInfos.add((certInfo =
                        new CertificateInfoImpl(CertificateUtils.readCertificate(certRecord.getCert(), CertificateType.X509, DataEncoding.DER))));

                    this.execMsgs.add(new ToolMessageImpl(ToolMessageLevel.INFO, String.format(
                        "DNS lookup (directAddr=%s) CERT record (certType=%d, keyAlg=%d) certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) processed.",
                        directAddr.toAddress(), certRecord.getCertType(), certRecord.getAlgorithm(), certInfo.getSubjectDn(), certInfo.getSerialNumber(),
                        certInfo.getIssuerDn())));
                } catch (CryptographyException e) {
                    this.execMsgs.add(new ToolMessageImpl(ToolMessageLevel.ERROR, String.format(
                        "DNS lookup (directAddr=%s) CERT record (certType=%d, keyAlg=%d) certificate processing failed: %s", directAddr.toAddress(),
                        certRecord.getCertType(), certRecord.getAlgorithm(), e.getMessage())));
                    this.execSuccess = false;

                    break;
                }
            }
        }

        return this.isSuccess();
    }

    @Override
    public boolean hasCertificateInfos() {
        return !CollectionUtils.isEmpty(this.certInfos);
    }

    @Nullable
    @Override
    public List<CertificateInfo> getCertificateInfos() {
        return this.certInfos;
    }
}
