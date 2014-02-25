package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseDnsCertificateLookupResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultHolder;
import org.xbill.DNS.CERTRecord;

public class ToolTestcaseDnsCertificateLookupResultStepImpl extends AbstractToolTestcaseCertificateResultStep implements
    ToolTestcaseDnsCertificateLookupResultStep {
    @Override
    public boolean execute(ToolTestcaseResultHolder resultHolder, MailAddress directAddr) {
        if (resultHolder.hasCertRecords()) {
            parseCertRecords(resultHolder);
        } else {
            this.setCertificateStatus(ToolTestcaseCertificateResultType.NO_CERT);
        }
        return this.getCertificateStatus().equals(ToolTestcaseCertificateResultType.VALID_CERT);
    }

    @Override
    public void parseCertRecords(ToolTestcaseResultHolder resultHolder) {
        for (CERTRecord certRecord : resultHolder.getCertRecords()) {
            switch (certRecord.getCertType()) {
                case CERTRecord.CertificateType.PKIX:
                    updateCertificateStatus(certRecord.getCert());
                    break;
                default:
                    this.setCertificateStatus(ToolTestcaseCertificateResultType.INCORRECT_CERT_TYPE);
                    break;
            }
        }
    }
}
