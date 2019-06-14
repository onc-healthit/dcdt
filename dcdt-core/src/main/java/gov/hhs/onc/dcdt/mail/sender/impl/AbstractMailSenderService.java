package gov.hhs.onc.dcdt.mail.sender.impl;

import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;

import com.sun.mail.smtp.SMTPSendFailedException;
import com.sun.mail.smtp.SMTPTransport;
import com.sun.mail.util.MailConnectException;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.dns.lookup.DnsNameService;
import gov.hhs.onc.dcdt.mail.JavaMailProperties;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailEncoding;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.MailRecipientType;
import gov.hhs.onc.dcdt.mail.ToolMailException;
import gov.hhs.onc.dcdt.mail.sender.MailSenderService;
import gov.hhs.onc.dcdt.mail.smtp.SmtpTransportProtocol;
import gov.hhs.onc.dcdt.mail.utils.ToolMailSessionUtils;
import gov.hhs.onc.dcdt.net.SslType;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;

public abstract class AbstractMailSenderService extends AbstractToolBean implements MailSenderService {
    @FunctionalInterface
    protected interface MailPreparator {
        public MailInfo prepareMail(MailInfo mailInfo) throws Exception;
    }

    protected static class ToolSmtpTransport extends SMTPTransport {
        protected SmtpTransportProtocol protocol;
        protected String host;
        protected int port;

        public ToolSmtpTransport(Session session, SmtpTransportProtocol protocol) {
            super(session, protocol.toUrlName(), protocol.getScheme(), (protocol.getSslType() == SslType.SSL));

            this.protocol = protocol;
        }

        @Override
        public synchronized void connect(String host, @Nonnegative int port, @Nullable String user, @Nullable String pass) throws MessagingException {
            super.connect((this.host = host), (this.port = port), user, pass);
        }

        public String getHost() {
            return this.host;
        }

        @Nonnegative
        public int getPort() {
            return this.port;
        }

        public SmtpTransportProtocol getProtocol() {
            return this.protocol;
        }
    }

    protected static class ToolSmtpsTransport extends ToolSmtpTransport {
        public ToolSmtpsTransport(Session session) {
            super(session, SmtpTransportProtocol.SMTPS);
        }
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    protected class RecipientMailPreparator implements MailPreparator {
        private MailAddress toAddr;

        public RecipientMailPreparator(MailAddress toAddr) {
            this.toAddr = toAddr;
        }

        @Override
        public MailInfo prepareMail(MailInfo mailInfo) throws Exception {
            mailInfo.setRecipients(new HashMap<>(Collections.singletonMap(MailRecipientType.TO, ArrayUtils.toArray(this.toAddr))));

            return mailInfo;
        }
    }

    @Order(Ordered.LOWEST_PRECEDENCE)
    protected class SentDateMailPreparator implements MailPreparator {
        @Override
        public MailInfo prepareMail(MailInfo mailInfo) throws Exception {
            mailInfo.setSentDate(new Date());

            return mailInfo;
        }
    }

    @Resource(name = "mailSessionDefault")
    protected Session session;

    protected int connTimeout;
    protected DnsNameService dnsNameService;
    protected MailEncoding enc;
    protected int readTimeout;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractMailSenderService.class);

    protected void send(MailInfo mailInfo, MailAddress fromAddr, MailAddress toAddr, String heloName, List<MailPreparator> mailPreps) throws MessagingException {
        ToolSmtpTransport transport =
            this.buildTransport(
                (mailInfo =
                    this.prepareMail(mailInfo,
                        ToolCollectionUtils.addAll(mailPreps, ToolArrayUtils.asList(new RecipientMailPreparator(toAddr), new SentDateMailPreparator())))),
                fromAddr, toAddr, heloName);

        if (transport == null) {
            throw new ToolMailException(String.format("Unable to build transport for mail MIME message (id=%s, from=%s, to=%s).", mailInfo.getMessageId(),
                mailInfo.getFrom(), mailInfo.getTo()));
        }

        try {
       //     transport.sendMessage(mailInfo.getMessage(), ArrayUtils.toArray(toAddr.toInternetAddress(mailInfo.getEncoding())));
        	
        	 String to = mailInfo.getTo().toString();

             // Sender's email ID needs to be mentioned
             String from = "sut.example@gmail.com";
             final String username = "sut.example@gmail.com";//change accordingly
             final String password = "smtptesting123";//change accordingly

        
             String host = "smtp.gmail.com";

             Properties props = new Properties();
             props.put("mail.smtp.auth", "true");
             props.put("mail.smtp.starttls.enable", "true");
             props.put("mail.smtp.host", host);
             props.put("mail.smtp.port", "25");

             // Get the Session object.
             Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                   protected PasswordAuthentication getPasswordAuthentication() {
                      return new PasswordAuthentication(username, password);
       	   }
                });
          
            
             // Create a default MimeMessage object.
      	   Message message = new MimeMessage(session);
      	
      	   // Set From: header field of the header.
      	   message.setFrom(new InternetAddress(from));
      	
      	   // Set To: header field of the header.
      	   message.setRecipients(Message.RecipientType.TO,
                     InternetAddress.parse(to));
      	
      	   // Set Subject: header field
      	   message.setSubject(mailInfo.getMessage().getSubject());
    
      	 
      	 
      	 message.setContent(mailInfo.getMessage().getContent(), mailInfo.getMessage().getContentType());

      	   // Send message
      	   Transport.send(message);

      	   System.out.println("Sent message successfully....");
        } catch (SMTPSendFailedException e) {
            throw new ToolMailException(
                String.format(
                    "Mail MIME message (id=%s, from=%s, to=%s) sender service transport (protocol=%s) connection (heloName=%s, host=%s, port=%d, from=%s, to=%s) send command (%s) failed (resultCode=%d).",
                    mailInfo.getMessageId(), mailInfo.getFrom(), mailInfo.getTo(), transport.getProtocol().getId(), heloName, transport.getHost(),
                    transport.getPort(), fromAddr, toAddr, e.getCommand(), e.getReturnCode()), e);
      
        	
        } catch (MessagingException e) {
            throw new ToolMailException(
                String.format(
                    "Mail MIME message (id=%s, from=%s, to=%s) sender service transport (protocol=%s) connection (heloName=%s, host=%s, port=%d, from=%s, to=%s) send failed.",
                    mailInfo.getMessageId(), mailInfo.getFrom(), mailInfo.getTo(), transport.getProtocol().getId(), heloName, transport.getHost(),
                    transport.getPort(), fromAddr, toAddr), e);
            
        } catch (Exception e) {
        	
        } finally {
            try {
                transport.close();
            } catch (Exception e) {
                LOGGER
                    .error(
                        String
                            .format(
                                "Unable to close mail MIME message (id=%s, from=%s, to=%s) sender service transport (protocol=%s) connection (heloName=%s, host=%s, port=%d, from=%s, to=%s).",
                                mailInfo.getMessageId(), mailInfo.getFrom(), mailInfo.getTo(), transport.getProtocol().getId(), heloName, transport.getHost(),
                                transport.getPort(), fromAddr, toAddr), e);
            }
        }
    }

    @Nullable
    protected abstract ToolSmtpTransport buildTransport(MailInfo mailInfo, MailAddress fromAddr, MailAddress toAddr, String heloName) throws MessagingException;

    @Nullable
    protected ToolSmtpTransport buildTransport(MailInfo mailInfo, SmtpTransportProtocol transportProtocol, String host, @Nonnegative int port,
        @Nullable String user, @Nullable String pass, MailAddress fromAddr, MailAddress toAddr, String heloName) throws MessagingException {
        SslType sslType = transportProtocol.getSslType();
        boolean ssl = (sslType == SslType.SSL);
        Session transportSession = ToolMailSessionUtils.buildSession(sslType);

        Properties transportProps = transportSession.getProperties();
        transportProps.put((ssl ? JavaMailProperties.SMTPS_CONNECTION_TIMEOUT_NAME : JavaMailProperties.SMTP_CONNECTION_TIMEOUT_NAME), this.connTimeout);
        // noinspection ConstantConditions
        transportProps.put((ssl ? JavaMailProperties.SMTPS_FROM_NAME : JavaMailProperties.SMTP_FROM_NAME), fromAddr.toAddress(BindingType.ADDRESS));
        transportProps.put((ssl ? JavaMailProperties.SMTPS_LOCALHOST_NAME : JavaMailProperties.SMTP_LOCALHOST_NAME), heloName);
        transportProps.put((ssl ? JavaMailProperties.SMTPS_TIMEOUT_NAME : JavaMailProperties.SMTP_TIMEOUT_NAME), this.readTimeout);

        if ((user != null) && (pass != null)) {
            transportProps.put((ssl ? JavaMailProperties.SMTPS_AUTH_NAME : JavaMailProperties.SMTP_AUTH_NAME), Boolean.TRUE);
        }

        ToolSmtpTransport transport = (ssl ? new ToolSmtpsTransport(transportSession) : new ToolSmtpTransport(transportSession, transportProtocol));

        try {
            transport.connect(host, port, user, pass);
            System.out.println("DEBUGGER**-->"+" "+host+" "+port+" "+user+" "+pass);
            return transport;
        } catch (Exception e) {
            if ((e instanceof MailConnectException) && (e.getCause() instanceof SocketTimeoutException)) {
                LOGGER
                    .warn(
                        String
                            .format(
                                "Mail MIME message (id=%s, from=%s, to=%s) sender service transport (protocol=%s) connection (heloName=%s, host=%s, port=%d, from=%s, to=%s) attempt timed out.",
                                mailInfo.getMessageId(), mailInfo.getFrom(), mailInfo.getTo(), transportProtocol.getId(), heloName, host, port, fromAddr,
                                toAddr), e);
            } else if (e instanceof AuthenticationFailedException) {
                LOGGER
                    .warn(
                        String
                            .format(
                                "Mail MIME message (id=%s, from=%s, to=%s) sender service transport (protocol=%s) connection (heloName=%s, host=%s, port=%d, from=%s, to=%s) authentication (user=%s) attempt failed.",
                                mailInfo.getMessageId(), mailInfo.getFrom(), mailInfo.getTo(), transportProtocol.getId(), heloName, host, port, fromAddr,
                                toAddr, user), e);
            } else {
                LOGGER
                    .warn(
                        String
                            .format(
                                "Mail MIME message (id=%s, from=%s, to=%s) sender service transport (protocol=%s) connection (heloName=%s, host=%s, port=%d, from=%s, to=%s) attempt failed.",
                                mailInfo.getMessageId(), mailInfo.getFrom(), mailInfo.getTo(), transportProtocol.getId(), heloName, host, port, fromAddr,
                                toAddr), e);
            }

            return null;
        }
    }

    protected MailInfo prepareMail(MailInfo mailInfo, List<MailPreparator> mailPreps) throws MessagingException {
        mailPreps.sort(AnnotationAwareOrderComparator.INSTANCE);

        for (MailPreparator mailPrep : mailPreps) {
            try {
                mailInfo = mailPrep.prepareMail(mailInfo);
            } catch (Exception e) {
                throw new ToolMailException(String.format("Unable to prepare (class=%s) mail MIME message (id=%s, from=%s, to=%s).", mailPrep.getClass()
                    .getName(), mailInfo.getMessageId(), mailInfo.getFrom(), mailInfo.getTo()), e);
            }
        }

        mailInfo.getMessage().saveChanges();

        return mailInfo;
    }

    @Nonnegative
    @Override
    public int getConnectTimeout() {
        return this.connTimeout;
    }

    @Override
    public void setConnectTimeout(@Nonnegative int connTimeout) {
        this.connTimeout = connTimeout;
    }

    @Override
    public DnsNameService getDnsNameService() {
        return this.dnsNameService;
    }

    @Override
    public void setDnsNameService(DnsNameService dnsNameService) {
        this.dnsNameService = dnsNameService;
    }

    @Override
    public MailEncoding getEncoding() {
        return this.enc;
    }

    @Override
    public void setEncoding(MailEncoding enc) {
        this.enc = enc;
    }

    @Nonnegative
    @Override
    public int getReadTimeout() {
        return this.readTimeout;
    }

    @Override
    public void setReadTimeout(@Nonnegative int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
