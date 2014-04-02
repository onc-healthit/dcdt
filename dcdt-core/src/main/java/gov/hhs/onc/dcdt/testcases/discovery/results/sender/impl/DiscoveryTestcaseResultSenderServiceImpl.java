package gov.hhs.onc.dcdt.testcases.discovery.results.sender.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.impl.MimeAttachmentResource;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.sender.impl.AbstractToolMailSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultCredentialType;
import gov.hhs.onc.dcdt.testcases.discovery.results.sender.DiscoveryTestcaseResultSenderService;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.ModelMap;

public class DiscoveryTestcaseResultSenderServiceImpl extends AbstractToolMailSenderService implements DiscoveryTestcaseResultSenderService {
    private final static DateFormat ATTACHMENT_RESOURCE_DESC_SUFFIX_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final static String ATTACHMENT_RESOURCE_DESC_SUFFIX_MAIL = " mail sent at ";

    private final static DateFormat ATTACHMENT_RESOURCE_FILE_NAME_SUFFIX_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HHmm_Z");
    private final static String ATTACHMENT_RESOURCE_FILE_NAME_SUFFIX_MAIL = "_mail_";

    private final static String MODEL_ATTR_NAME_TESTCASE_DISCOVERY_RESULT = "discoveryTestcaseResult";

    public DiscoveryTestcaseResultSenderServiceImpl(Charset mailEnc, VelocityEngine velocityEngine, InstanceMailAddressConfig fromConfig,
        @Nullable InstanceMailAddressConfig replyToConfig, String mimeMailMsgBeanName) {
        super(mailEnc, velocityEngine, fromConfig, replyToConfig, mimeMailMsgBeanName);
    }

    @Override
    public void send(DiscoveryTestcaseResult discoveryTestcaseResult, MailAddress to) throws Exception {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(MODEL_ATTR_NAME_TESTCASE_DISCOVERY_RESULT, discoveryTestcaseResult);

        // noinspection ConstantConditions
        List<MimeAttachmentResource> attachmentResources = new ArrayList<>(DiscoveryTestcaseResultCredentialType.values().length + 1);
        MimeAttachmentResource attachmentResource;

        for (DiscoveryTestcaseResultCredentialType discoveryTestcaseResultCredType : EnumSet.allOf(DiscoveryTestcaseResultCredentialType.class)) {
            if ((attachmentResource = buildDiscoveryTestcaseResultCredentialAttachment(discoveryTestcaseResult, discoveryTestcaseResultCredType)) != null) {
                attachmentResources.add(attachmentResource);
            }
        }

        MailInfo mailInfo;

        // noinspection ConstantConditions
        if (discoveryTestcaseResult.hasMailInfo() && (mailInfo = discoveryTestcaseResult.getMailInfo()).hasMessageHelper()) {
            // noinspection ConstantConditions
            ToolMimeMessageHelper msgHelper = mailInfo.getMessageHelper();
            // noinspection ConstantConditions
            MimeMessage msg = msgHelper.getMimeMessage();
            Date msgSentDate = msg.getSentDate();
            // noinspection ConstantConditions
            String discoveryTestcaseName = discoveryTestcaseResult.getTestcase().getName();

            // noinspection ConstantConditions
            attachmentResources.add(new MimeAttachmentResource(msgHelper.write(),
                (discoveryTestcaseName + ATTACHMENT_RESOURCE_DESC_SUFFIX_MAIL + ATTACHMENT_RESOURCE_DESC_SUFFIX_DATE_FORMAT.format(msgSentDate)),
                MailContentTypes.MSG_RFC822, (discoveryTestcaseName.toLowerCase() + ATTACHMENT_RESOURCE_FILE_NAME_SUFFIX_MAIL
                    + ATTACHMENT_RESOURCE_FILE_NAME_SUFFIX_DATE_FORMAT.format(msgSentDate) + ToolMimeMessageHelper.FILE_EXT_MAIL)));
        }

        this.send(modelMap, modelMap, to, null, null, attachmentResources);
    }

    @Nullable
    private static MimeAttachmentResource buildDiscoveryTestcaseResultCredentialAttachment(DiscoveryTestcaseResult discoveryTestcaseResult,
        DiscoveryTestcaseResultCredentialType discoveryTestcaseResultCredType) throws Exception {
        DiscoveryTestcaseCredential discoveryTestcaseResultCred;
        CredentialInfo discoveryTestcaseResultCredInfo;
        CertificateInfo discoveryTestcaseResultCredCertInfo;

        // noinspection ConstantConditions
        return ((discoveryTestcaseResult.hasCredential(discoveryTestcaseResultCredType)
            && (discoveryTestcaseResultCred = discoveryTestcaseResult.getCredential(discoveryTestcaseResultCredType)).hasCredentialInfo()
            && (discoveryTestcaseResultCredInfo = discoveryTestcaseResultCred.getCredentialInfo()).hasCertificateDescriptor() && (discoveryTestcaseResultCredCertInfo =
            discoveryTestcaseResultCredInfo.getCertificateDescriptor()).hasCertificate()) ? new MimeAttachmentResource(CertificateUtils.writeCertificate(
            discoveryTestcaseResultCredCertInfo.getCertificate(), DataEncoding.DER), discoveryTestcaseResultCred.getName(), discoveryTestcaseResultCredCertInfo
            .getCertificateType().getContentType(), discoveryTestcaseResult.getTestcase().getName()
            + discoveryTestcaseResultCredType.getAttachmentFileNameSuffix() + DataEncoding.DER.getFileExtension()) : null);
    }
}
