package gov.hhs.onc.dcdt.testcases.results;

import gov.hhs.onc.dcdt.mail.MailAddress;

public interface ToolTestcaseDnsCertificateLookupResultStep extends ToolTestcaseCertificateResultStep {
    public void parseCertRecords(ToolTestcaseResultHolder resultHolder, MailAddress mailAddr);
}
