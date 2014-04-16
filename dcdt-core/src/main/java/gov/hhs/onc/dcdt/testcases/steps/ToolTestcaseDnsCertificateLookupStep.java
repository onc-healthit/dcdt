package gov.hhs.onc.dcdt.testcases.steps;

import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.List;
import org.xbill.DNS.CERTRecord;

public interface ToolTestcaseDnsCertificateLookupStep extends ToolTestcaseCertificateStep {
    public void parseCertRecords(MailAddress mailAddr, List<CERTRecord> certRecords);
}
