package gov.hhs.onc.dcdt.mail.sender.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailTransportListener;
import gov.hhs.onc.dcdt.mail.config.MailGatewayConfig;
import gov.hhs.onc.dcdt.mail.config.MailGatewayCredentialConfig;
import gov.hhs.onc.dcdt.mail.impl.MimeAttachmentResource;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.sender.ToolMailSenderService;
import gov.hhs.onc.dcdt.mail.sender.ToolMimeMailMessage;
import gov.hhs.onc.dcdt.mail.sender.ToolMimeMessagePreparator;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.nio.charset.Charset;
import java.util.List;
import javax.annotation.Nullable;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import org.apache.commons.collections4.CollectionUtils;
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
        public void send(ToolMimeMailMessage mimeMailMsg, ToolMimeMessagePreparator ... mimeMsgPreps) throws MailException {
            for (ToolMimeMessagePreparator mimeMsgPrep : mimeMsgPreps) {
                try {
                    mimeMsgPrep.prepare();
                } catch (Exception e) {
                    throw new MailPreparationException(String.format("Unable to prepare (class=%s) mail MIME message:\n%s",
                        ToolClassUtils.getName(mimeMsgPrep), mimeMailMsg), e);
                }
            }

            this.send(mimeMailMsg.getMimeMessage());
        }

        @Override
        protected Transport getTransport(Session mailSession) throws NoSuchProviderException {
            Transport transport = super.getTransport(mailSession);

            if (AbstractToolMailSenderService.this.hasTransportListenerBeanNames()) {
                for (String transportListenerBeanName : AbstractToolMailSenderService.this.transportListenerBeanNames) {
                    transport.addTransportListener(ToolBeanFactoryUtils.createBean(AbstractToolMailSenderService.this.appContext, transportListenerBeanName,
                        MailTransportListener.class, AbstractToolMailSenderService.this.mailEnc));
                }
            }

            return transport;
        }
    }

    protected AbstractApplicationContext appContext;
    protected List<String> transportListenerBeanNames;
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

    protected void send(@Nullable ModelMap subjModelMap, @Nullable ModelMap textModelMap, MailAddress to, @Nullable MimeAttachmentResource ... attachments)
        throws Exception {
        this.send(subjModelMap, textModelMap, to, ToolArrayUtils.asList(attachments));
    }

    protected void
        send(@Nullable ModelMap subjModelMap, @Nullable ModelMap textModelMap, MailAddress to, @Nullable Iterable<MimeAttachmentResource> attachments)
            throws Exception {
        MailGatewayConfig mailGatewayConfig = fromConfig.getGatewayConfig();
        MailGatewayCredentialConfig mailGatewayCredConfig = fromConfig.getGatewayCredentialConfig();
        Session mailSession = mailGatewayConfig.getSession();

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

        mailSender.send(mimeMailMsg,
            ToolBeanFactoryUtils.createBeanOfType(this.appContext, ToolMimeMessagePreparator.class, this.velocityEngine, mimeMailMsg, to, attachments));
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

    @Override
    public boolean hasTransportListenerBeanNames() {
        return !CollectionUtils.isEmpty(this.transportListenerBeanNames);
    }

    @Nullable
    @Override
    public List<String> getTransportListenerBeanNames() {
        return this.transportListenerBeanNames;
    }

    @Override
    public void setTransportListenerBeanNames(@Nullable List<String> transportListenerBeanNames) {
        this.transportListenerBeanNames = transportListenerBeanNames;
    }
}
