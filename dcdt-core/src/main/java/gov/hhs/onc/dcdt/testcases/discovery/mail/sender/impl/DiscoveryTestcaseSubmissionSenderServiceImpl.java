package gov.hhs.onc.dcdt.testcases.discovery.mail.sender.impl;

import gov.hhs.onc.dcdt.crypto.EncryptionAlgorithm;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.crypto.utils.ToolSmimeUtils;
import gov.hhs.onc.dcdt.mail.sender.impl.AbstractMailTemplateSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.mail.sender.DiscoveryTestcaseSubmissionSenderService;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import javax.annotation.Nullable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ui.ModelMap;

public class DiscoveryTestcaseSubmissionSenderServiceImpl extends AbstractMailTemplateSenderService implements DiscoveryTestcaseSubmissionSenderService {
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    private class DiscoveryTestcaseSubmissionMailPreparator implements MailPreparator {
        private CredentialInfo signerCredInfo;
        private CertificateInfo encryptionCertInfo;
        private EncryptionAlgorithm encryptionAlg;

        public DiscoveryTestcaseSubmissionMailPreparator(@Nullable CredentialInfo signerCredInfo, @Nullable CertificateInfo encryptionCertInfo,
            @Nullable EncryptionAlgorithm encryptionAlg) {
            this.signerCredInfo = signerCredInfo;
            this.encryptionCertInfo = encryptionCertInfo;
            this.encryptionAlg = encryptionAlg;
        }

        @Override
        public MailInfo prepareMail(MailInfo mailInfo) throws Exception {
            return (((this.signerCredInfo != null) && (this.encryptionCertInfo != null) && (this.encryptionAlg != null)) ? ToolSmimeUtils.signAndEncrypt(
                mailInfo, signerCredInfo, encryptionCertInfo, encryptionAlg) : mailInfo);
        }
    }

    private final static String MODEL_ATTR_NAME_TESTCASE_DISCOVERY_SUBMISSION = "discoveryTestcaseSubmission";

    @Override
    public void send(DiscoveryTestcaseSubmission submission, MailAddress toAddr) throws Exception {
        this.send(submission, toAddr, null, null, null);
    }

    @Override
    public void send(DiscoveryTestcaseSubmission submission, MailAddress toAddr, @Nullable CredentialInfo signerCredInfo,
        @Nullable CertificateInfo encryptionCertInfo, @Nullable EncryptionAlgorithm encryptionAlg) throws Exception {
        ModelMap model = new ModelMap();
        model.addAttribute(MODEL_ATTR_NAME_TESTCASE_DISCOVERY_SUBMISSION, submission);

        this.send(toAddr, model, model, ToolArrayUtils.asList(new DiscoveryTestcaseSubmissionMailPreparator(signerCredInfo, encryptionCertInfo, encryptionAlg)));
    }
}
