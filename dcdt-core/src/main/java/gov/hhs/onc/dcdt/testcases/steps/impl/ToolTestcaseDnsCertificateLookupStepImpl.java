package gov.hhs.onc.dcdt.testcases.steps.impl;

import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseDnsCertificateLookupStep;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseDnsLookupStep;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import java.util.List;
import org.xbill.DNS.CERTRecord;

public class ToolTestcaseDnsCertificateLookupStepImpl extends AbstractToolTestcaseCertificateStep implements ToolTestcaseDnsCertificateLookupStep {
    @Override
    public boolean execute(MailAddress directAddr, ToolTestcaseStep prevStep) {
        if (prevStep instanceof ToolTestcaseDnsLookupStep && ((ToolTestcaseDnsLookupStep) prevStep).hasRecords()) {
            List<CERTRecord> certRecords = ((ToolTestcaseDnsLookupStep) prevStep).getRecords();
            parseCertRecords(directAddr.forBindingType(this.bindingType), certRecords);
        } else {
            this.setCertificateStatus(ToolTestcaseCertificateResultType.NO_CERT);
        }

        return this.getCertificateStatus().equals(ToolTestcaseCertificateResultType.VALID_CERT);
    }

    @Override
    public void parseCertRecords(MailAddress mailAddr, List<CERTRecord> certRecords) {
        for (CERTRecord certRecord : certRecords) {
            switch (certRecord.getCertType()) {
                case CERTRecord.CertificateType.PKIX:
                    updateCertificateStatus(certRecord.getCert(), mailAddr);
                    break;
                default:
                    this.setCertificateStatus(ToolTestcaseCertificateResultType.INCORRECT_CERT_TYPE);
                    break;
            }
        }
    }
}
