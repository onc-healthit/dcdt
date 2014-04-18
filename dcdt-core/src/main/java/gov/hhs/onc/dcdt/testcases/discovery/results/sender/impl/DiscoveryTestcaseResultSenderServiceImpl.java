package gov.hhs.onc.dcdt.testcases.discovery.results.sender.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.impl.MimeAttachmentResource;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.sender.impl.AbstractToolMailSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultCredentialType;
import gov.hhs.onc.dcdt.testcases.discovery.results.sender.DiscoveryTestcaseResultSenderService;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        DiscoveryTestcaseSubmission discoveryTestcaseSubmission = discoveryTestcaseResult.getSubmission();
        DiscoveryTestcase discoveryTestcase = discoveryTestcaseSubmission.getTestcase();
        // noinspection ConstantConditions
        String discoveryTestcaseName = discoveryTestcase.getName();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(MODEL_ATTR_NAME_TESTCASE_DISCOVERY_RESULT, discoveryTestcaseResult);

        // noinspection ConstantConditions
        List<MimeAttachmentResource> attachmentResources = new ArrayList<>(DiscoveryTestcaseResultCredentialType.values().length + 1);

        if (discoveryTestcaseResult.hasExpectedDecryptionCredential()) {
            DiscoveryTestcaseCredential expectedDecryptCred = discoveryTestcaseResult.getExpectedDecryptionCredential();
            // noinspection ConstantConditions
            CertificateInfo expectedDecryptCredCertInfo = expectedDecryptCred.getCredentialInfo().getCertificateDescriptor();

            // noinspection ConstantConditions
            attachmentResources.add(new MimeAttachmentResource(
                CertificateUtils.writeCertificate(expectedDecryptCredCertInfo.getCertificate(), DataEncoding.DER), expectedDecryptCred.getName(),
                expectedDecryptCredCertInfo.getCertificateType().getContentType(), discoveryTestcaseName
                    + DiscoveryTestcaseResultCredentialType.EXPECTED.getAttachmentFileNameSuffix() + DataEncoding.DER.getFileExtension()));
        }

        if (discoveryTestcaseResult.hasDecryptionCredential()) {
            DiscoveryTestcaseCredential decryptCred = discoveryTestcaseResult.getDecryptionCredential();
            // noinspection ConstantConditions
            CertificateInfo decryptCredCertInfo = decryptCred.getCredentialInfo().getCertificateDescriptor();

            // noinspection ConstantConditions
            attachmentResources.add(new MimeAttachmentResource(CertificateUtils.writeCertificate(decryptCredCertInfo.getCertificate(), DataEncoding.DER),
                decryptCred.getName(), decryptCredCertInfo.getCertificateType().getContentType(), discoveryTestcaseName
                    + DiscoveryTestcaseResultCredentialType.DISCOVERED.getAttachmentFileNameSuffix() + DataEncoding.DER.getFileExtension()));
        }

        ToolMimeMessageHelper msgHelper = discoveryTestcaseSubmission.getMessageHelper();
        MimeMessage msg = msgHelper.getMimeMessage();
        Date msgSentDate = msg.getSentDate();

        // noinspection ConstantConditions
        attachmentResources.add(new MimeAttachmentResource(msgHelper.write(),
            (discoveryTestcaseName + ATTACHMENT_RESOURCE_DESC_SUFFIX_MAIL + ATTACHMENT_RESOURCE_DESC_SUFFIX_DATE_FORMAT.format(msgSentDate)),
            MailContentTypes.MSG_RFC822, (discoveryTestcaseName.toLowerCase() + ATTACHMENT_RESOURCE_FILE_NAME_SUFFIX_MAIL
                + ATTACHMENT_RESOURCE_FILE_NAME_SUFFIX_DATE_FORMAT.format(msgSentDate) + ToolMimeMessageHelper.FILE_EXT_MAIL)));

        this.send(modelMap, modelMap, to, attachmentResources);
    }
}
