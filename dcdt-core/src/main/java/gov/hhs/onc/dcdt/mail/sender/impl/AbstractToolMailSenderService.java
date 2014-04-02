package gov.hhs.onc.dcdt.mail.sender.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.JavaMailProperties;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.config.MailGatewayConfig;
import gov.hhs.onc.dcdt.mail.config.MailGatewayCredentialConfig;
import gov.hhs.onc.dcdt.mail.crypto.utils.ToolSmimeUtils;
import gov.hhs.onc.dcdt.mail.impl.MimeAttachmentResource;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.sender.ToolMailSenderService;
import gov.hhs.onc.dcdt.mail.sender.ToolMimeMailMessage;
import gov.hhs.onc.dcdt.mail.sender.ToolMimeMessagePreparator;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.ModelMap;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolMailSenderService extends AbstractToolBean implements ToolMailSenderService {
    protected class ToolMailSender extends JavaMailSenderImpl {
        public void send(ToolMimeMailMessage mimeMailMsg, @Nullable CredentialInfo signingCredInfo, @Nullable CertificateInfo encryptingCertInfo,
            ToolMimeMessagePreparator ... mimeMsgPreps) throws MailException, MessagingException, IOException {
            for (ToolMimeMessagePreparator mimeMsgPrep : mimeMsgPreps) {
                try {
                    mimeMsgPrep.prepare();
                } catch (Exception e) {
                    throw new MailPreparationException(String.format("Unable to prepare (class=%s) mail MIME message:\n%s",
                        ToolClassUtils.getName(mimeMsgPrep), mimeMailMsg), e);
                }
            }

            MimeMessage mimeMsg = mimeMailMsg.getMimeMessage();
            ToolMimeMessageHelper msgHelper = new ToolMimeMessageHelper(mimeMsg, MailContentTypes.MAIL_ENCODING_UTF8);

            if (signingCredInfo != null) {
                // noinspection ConstantConditions
                msgHelper =
                    ToolSmimeUtils.sign(msgHelper, signingCredInfo.getKeyDescriptor().getPrivateKey(), signingCredInfo.getCertificateDescriptor()
                        .getCertificate());
            }

            if (encryptingCertInfo != null) {
                msgHelper = ToolSmimeUtils.encrypt(msgHelper, encryptingCertInfo.getCertificate());
            }

            this.send(msgHelper.getMimeMessage());
        }
    }

    protected AbstractApplicationContext appContext;
    protected Charset mailEnc;
    protected VelocityEngine velocityEngine;
    protected InstanceMailAddressConfig fromConfig;
    protected InstanceMailAddressConfig replyToConfig;
    protected String mimeMailMsgBeanName;

    protected AbstractToolMailSenderService(Charset mailEnc, VelocityEngine velocityEngine, InstanceMailAddressConfig fromConfig,
        @Nullable InstanceMailAddressConfig replyToConfig, String mimeMailMsgBeanName) {
        this.mailEnc = mailEnc;
        this.velocityEngine = velocityEngine;
        this.fromConfig = fromConfig;
        this.replyToConfig = replyToConfig;
        this.mimeMailMsgBeanName = mimeMailMsgBeanName;
    }

    protected void send(@Nullable ModelMap subjModelMap, @Nullable ModelMap textModelMap, MailAddress to, @Nullable CredentialInfo signingCredInfo,
        @Nullable CertificateInfo encryptingCertInfo, @Nullable MimeAttachmentResource ... attachmentResources) throws Exception {
        this.send(subjModelMap, textModelMap, to, signingCredInfo, encryptingCertInfo, ToolArrayUtils.asList(attachmentResources));
    }

    protected void send(@Nullable ModelMap subjModelMap, @Nullable ModelMap textModelMap, MailAddress to, @Nullable CredentialInfo signingCredInfo,
        @Nullable CertificateInfo encryptingCertInfo, @Nullable Iterable<MimeAttachmentResource> attachmentResources) throws Exception {
        MailGatewayConfig mailGatewayConfig = fromConfig.getGatewayConfig();
        MailGatewayCredentialConfig mailGatewayCredConfig = fromConfig.getGatewayCredentialConfig();

        Session mailSession = mailGatewayConfig.getSession();
        mailSession.getProperties().put(JavaMailProperties.FROM, this.fromConfig.getMailAddress().toAddress());

        ToolMailSender mailSender = new ToolMailSender();
        mailSender.setSession(mailSession);
        // noinspection ConstantConditions
        mailSender.setHost(mailGatewayConfig.getHost(true).getHostAddress());
        mailSender.setPort(mailGatewayConfig.getPort());
        mailSender.setProtocol(mailGatewayConfig.getTransportProtocol().getProtocol());
        // noinspection ConstantConditions
        mailSender.setUsername(mailGatewayCredConfig.getId().toAddress());
        mailSender.setPassword(mailGatewayCredConfig.getSecret());

        ToolMimeMailMessage mimeMailMsg =
            ToolBeanFactoryUtils.createBean(this.appContext, this.mimeMailMsgBeanName, ToolMimeMailMessage.class, new ToolMimeMessageHelper(mailSession,
                this.mailEnc), this.fromConfig, this.replyToConfig, subjModelMap, textModelMap);

        mailSender.send(mimeMailMsg, signingCredInfo, encryptingCertInfo,
            ToolBeanFactoryUtils.createBeanOfType(this.appContext, ToolMimeMessagePreparator.class, this.velocityEngine, mimeMailMsg, to, attachmentResources));
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public InstanceMailAddressConfig getFromConfig() {
        return this.fromConfig;
    }

    @Override
    public boolean hasReplyToConfig() {
        return (this.replyToConfig != null);
    }

    @Nullable
    @Override
    public InstanceMailAddressConfig getReplyToConfig() {
        return this.replyToConfig;
    }
}
