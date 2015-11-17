package gov.hhs.onc.dcdt.testcases.discovery.results.sender.impl;

import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailContentTransferEncoding;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.impl.MimeAttachmentResource;
import gov.hhs.onc.dcdt.mail.sender.impl.AbstractTemplateMailSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultCredentialType;
import gov.hhs.onc.dcdt.testcases.discovery.results.sender.DiscoveryTestcaseResultSenderService;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ui.ModelMap;

public class DiscoveryTestcaseResultSenderServiceImpl extends AbstractTemplateMailSenderService implements DiscoveryTestcaseResultSenderService {
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    private class DiscoveryTestcaseResultMailPreparator implements MailPreparator {
        private DiscoveryTestcaseResult discoveryTestcaseResult;

        public DiscoveryTestcaseResultMailPreparator(DiscoveryTestcaseResult discoveryTestcaseResult) {
            this.discoveryTestcaseResult = discoveryTestcaseResult;
        }

        @Override
        public MailInfo prepareMail(MailInfo mailInfo) throws Exception {
            DiscoveryTestcaseSubmission discoveryTestcaseSubmission = discoveryTestcaseResult.getSubmission();
            DiscoveryTestcase discoveryTestcase = discoveryTestcaseSubmission.getTestcase();
            // noinspection ConstantConditions
            String discoveryTestcaseName = discoveryTestcase.getName();

            // noinspection ConstantConditions
            List<MimeAttachmentResource> attachments = new ArrayList<>(DiscoveryTestcaseResultCredentialType.values().length + 1);

            if (!discoveryTestcaseResult.isSuccess() && discoveryTestcaseResult.hasExpectedDecryptionCredential()) {
                DiscoveryTestcaseCredential expectedDecryptCred = discoveryTestcaseResult.getExpectedDecryptionCredential();
                // noinspection ConstantConditions
                CertificateInfo expectedDecryptCredCertInfo = expectedDecryptCred.getCredentialInfo().getCertificateDescriptor();

                // noinspection ConstantConditions
                attachments
                    .add(new MimeAttachmentResource(CertificateUtils.writeCertificate(expectedDecryptCredCertInfo.getCertificate(), DataEncoding.DER),
                        expectedDecryptCred.getName(), MailContentTransferEncoding.BASE64, expectedDecryptCredCertInfo.getCertificateType().getContentType(),
                        discoveryTestcaseName + DiscoveryTestcaseResultCredentialType.EXPECTED.getAttachmentFileNameSuffix()
                            + DataEncoding.DER.getFileExtension()));
            }

            if (discoveryTestcaseResult.hasDecryptionCredential()) {
                DiscoveryTestcaseCredential decryptCred = discoveryTestcaseResult.getDecryptionCredential();
                // noinspection ConstantConditions
                CertificateInfo decryptCredCertInfo = decryptCred.getCredentialInfo().getCertificateDescriptor();

                // noinspection ConstantConditions
                attachments.add(new MimeAttachmentResource(CertificateUtils.writeCertificate(decryptCredCertInfo.getCertificate(), DataEncoding.DER),
                    decryptCred.getName(), MailContentTransferEncoding.BASE64, decryptCredCertInfo.getCertificateType().getContentType(), discoveryTestcaseName
                        + DiscoveryTestcaseResultCredentialType.DISCOVERED.getAttachmentFileNameSuffix() + DataEncoding.DER.getFileExtension()));
            }

            Date msgProcDate = new Date();

            // noinspection ConstantConditions
            attachments.add(new MimeAttachmentResource(discoveryTestcaseSubmission.getMailInfo().write(), (discoveryTestcaseName
                + ATTACHMENT_RESOURCE_DESC_SUFFIX_MAIL + ATTACHMENT_RESOURCE_DESC_SUFFIX_DATE_FORMAT.format(msgProcDate)),
                MailContentTransferEncoding.QUOTED_PRINTABLE, MailContentTypes.MSG_RFC822, (discoveryTestcaseName.toLowerCase()
                    + ATTACHMENT_RESOURCE_FILE_NAME_SUFFIX_MAIL + ATTACHMENT_RESOURCE_FILE_NAME_SUFFIX_DATE_FORMAT.format(msgProcDate)
                    + FilenameUtils.EXTENSION_SEPARATOR + FILE_EXT_MAIL)));

            mailInfo.setAttachments(attachments);

            return mailInfo;
        }
    }

    private final static DateFormat ATTACHMENT_RESOURCE_DESC_SUFFIX_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final static String ATTACHMENT_RESOURCE_DESC_SUFFIX_MAIL = " mail processed at ";

    private final static DateFormat ATTACHMENT_RESOURCE_FILE_NAME_SUFFIX_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HHmm_Z");
    private final static String ATTACHMENT_RESOURCE_FILE_NAME_SUFFIX_MAIL = "_mail_";

    private final static String MODEL_ATTR_NAME_TESTCASE_DISCOVERY_RESULT = "discoveryTestcaseResult";

    private final static String FILE_EXT_MAIL = "eml";

    @Override
    public void send(DiscoveryTestcaseResult discoveryTestcaseResult, MailAddress toAddr) throws MessagingException {
        ModelMap model = new ModelMap();
        model.addAttribute(MODEL_ATTR_NAME_TESTCASE_DISCOVERY_RESULT, discoveryTestcaseResult);

        this.send(toAddr, model, model, ToolArrayUtils.asList(new DiscoveryTestcaseResultMailPreparator(discoveryTestcaseResult)));
    }
}
