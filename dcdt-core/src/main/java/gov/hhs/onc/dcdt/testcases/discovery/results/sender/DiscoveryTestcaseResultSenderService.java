package gov.hhs.onc.dcdt.testcases.discovery.results.sender;

import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.sender.MailTemplateSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import javax.mail.MessagingException;

public interface DiscoveryTestcaseResultSenderService extends MailTemplateSenderService {
    public void send(DiscoveryTestcaseResult discoveryTestcaseResult, MailAddress toAddr) throws MessagingException;
}
