package gov.hhs.onc.dcdt.service.mail.server.impl;

import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.sender.impl.AbstractMailSenderService;
import gov.hhs.onc.dcdt.mail.smtp.SmtpTransportProtocol;
import gov.hhs.onc.dcdt.service.mail.server.RemoteMailSenderService;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.MXRecord;

public class RemoteMailSenderServiceImpl extends AbstractMailSenderService implements RemoteMailSenderService {
    private final static Logger LOGGER = LoggerFactory.getLogger(RemoteMailSenderServiceImpl.class);

    @Override
    public void send(MailInfo mailInfo, MailAddress fromAddr, MailAddress toAddr, String heloName) throws MessagingException {
        this.send(mailInfo, fromAddr, toAddr, heloName, new ArrayList<>());
    }

    @Nullable
    @Override
    protected ToolSmtpTransport buildTransport(MailInfo mailInfo, MailAddress fromAddr, MailAddress toAddr, String heloName) throws MessagingException {
        List<MXRecord> toMxRecords = this.dnsNameService.lookupRecords(DnsRecordType.MX, MXRecord.class, toAddr.getDomainName(), -1);

        if (CollectionUtils.isEmpty(toMxRecords)) {
            LOGGER.error(String.format("No DNS MX record(s) available for mail MIME message (id=%s, from=%s, to=%s) recipient address (%s) domain name.",
                mailInfo.getMessageId(), mailInfo.getFrom(), mailInfo.getTo(), toAddr));

            return null;
        }

        ToolSmtpTransport transport = null;

        for (MXRecord toMxRecord : toMxRecords) {
            try {
                for (InetAddress toConnAddr : this.dnsNameService.getAllByName(toMxRecord.getTarget().toString(false))) {
                    if ((transport =
                        this.buildTransport(mailInfo, SmtpTransportProtocol.SMTP, toConnAddr.getHostAddress(), SmtpTransportProtocol.SMTP.getDefaultPort(),
                            null, null, fromAddr, toAddr, heloName)) != null) {
                        break;
                    }
                }
            } catch (UnknownHostException ignored) {
            }
        }

        return transport;
    }
}
