package gov.hhs.onc.dcdt.testcases.discovery.results.sender;

import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.sender.ToolMailSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;

public interface DiscoveryTestcaseResultSenderService extends ToolMailSenderService {
    public void send(DiscoveryTestcase discoveryTestcase, DiscoveryTestcaseResult discoveryTestcaseResult, MailAddress to) throws Exception;
}
