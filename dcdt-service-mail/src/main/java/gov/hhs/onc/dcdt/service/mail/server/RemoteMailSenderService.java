package gov.hhs.onc.dcdt.service.mail.server;

import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.sender.MailSenderService;
import javax.mail.MessagingException;

public interface RemoteMailSenderService extends MailSenderService {
    public void send(MailInfo mailInfo, MailAddress fromAddr, MailAddress toAddr, String heloName) throws MessagingException;
}
