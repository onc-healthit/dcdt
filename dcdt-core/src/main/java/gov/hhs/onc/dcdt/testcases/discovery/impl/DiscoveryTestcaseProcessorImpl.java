package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.crypto.ToolSmimeException;
import gov.hhs.onc.dcdt.mail.crypto.utils.ToolSmimeUtils;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.utils.ToolMimePartUtils;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential.DiscoveryTestcaseCredentialValidPredicate;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseProcessor;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.mail.internet.MimeBodyPart;
import org.apache.commons.collections4.CollectionUtils;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseProcImpl")
public class DiscoveryTestcaseProcessorImpl extends
    AbstractToolTestcaseProcessor<DiscoveryTestcaseDescription, DiscoveryTestcase, DiscoveryTestcaseSubmission, DiscoveryTestcaseResult> implements
    DiscoveryTestcaseProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(DiscoveryTestcaseProcessorImpl.class);

    public DiscoveryTestcaseProcessorImpl() {
        super(DiscoveryTestcaseResult.class);
    }

    @Override
    public DiscoveryTestcaseResult process(DiscoveryTestcaseSubmission submission) {
        DiscoveryTestcase testcase = submission.getTestcase();
        DiscoveryTestcaseResult result = this.createResult(submission);
        ToolMimeMessageHelper msgHelper = submission.getMessageHelper();
        String msgId = null;
        MailAddress msgFrom = null, msgTo = null;
        List<String> procMsgs = new ArrayList<>();
        boolean procSuccess = true;

        try {
            msgId = msgHelper.getMimeMessage().getMessageID();
            msgFrom = msgHelper.getFrom();
            msgTo = msgHelper.getTo();

            if (testcase == null) {
                throw new ToolSmimeException(String.format("Unable to find a matching Discovery testcase for mail MIME message (id=%s, from=%s, to=%s).",
                    msgId, msgFrom, msgTo));
            }

            MimeBodyPart decryptedBodyPart = this.decrypt(msgHelper, msgId, msgFrom, msgTo, testcase, result);

            this.verifySender(msgId, msgFrom, msgTo, result, decryptedBodyPart, this.verifySigners(msgHelper, msgId, msgFrom, msgTo, decryptedBodyPart));
        } catch (Exception e) {
            procMsgs.add(e.getMessage());
            procSuccess = false;
        }

        result.setProcessingMessages(procMsgs);
        result.setProcessingSuccess((procSuccess && (result.getDecryptionCredential() == result.getExpectedDecryptionCredential())));

        LOGGER.info(String.format("Processed (success=%s) Discovery testcase (name=%s) for mail MIME message (id=%s, from=%s, to=%s): [%s]",
            result.isSuccess(), ((testcase != null) ? testcase.getName() : null), msgId, msgFrom, msgTo,
            ToolStringUtils.joinDelimit(result.getMessages(), "; ")));

        return result;
    }

    private void verifySender(String msgId, MailAddress msgFrom, MailAddress msgTo, DiscoveryTestcaseResult result, MimeBodyPart decryptedBodyPart,
        Map<SignerId, CertificateInfo> signerCertMap) throws Exception {
        result.getProcessedSteps().addAll(this.certDiscoveryService.discoverCertificates(msgFrom));

        if (!result.isSuccess() || !result.hasDiscoveredCertificateInfo()) {
            throw new ToolSmimeException(String.format("Unable to discover mail MIME message (id=%s, from=%s, to=%s) sender certificate.", msgId, msgFrom,
                msgTo));
        }

        CertificateInfo senderCertInfo = result.getDiscoveredCertificateInfo();

        if (!signerCertMap.containsValue(senderCertInfo)) {
            // noinspection ConstantConditions
            throw new ToolSmimeException(
                String
                    .format(
                        "Mail MIME message (id=%s, from=%s, to=%s) signed content (type=%s) was not signed by the discovered sender certificate (subj={%s}, issuer={%s}, serialNum=%s).",
                        msgId, msgFrom, msgTo, ToolMimePartUtils.getContentType(decryptedBodyPart), senderCertInfo.getSubjectName(),
                        senderCertInfo.getIssuerName(), senderCertInfo.getSerialNumber()));
        }

        result.setSignerCertificateInfo(senderCertInfo);
    }

    private Map<SignerId, CertificateInfo> verifySigners(ToolMimeMessageHelper msgHelper, String msgId, MailAddress msgFrom, MailAddress msgTo,
        MimeBodyPart decryptedBodyPart) throws Exception {
        SMIMESigned signed = ToolSmimeUtils.getSigned(msgHelper, decryptedBodyPart);

        LOGGER.info(String.format("Extracted mail MIME message (id=%s, from=%s, to=%s) signed content (type=%s).", msgId, msgFrom, msgTo,
            ToolMimePartUtils.getContentType(signed.getContent())));

        Map<SignerId, CertificateInfo> signerCertMap = ToolSmimeUtils.verifySignatures(signed);

        if (signerCertMap.isEmpty()) {
            throw new ToolSmimeException(String.format("Mail MIME message (id=%s, from=%s, to=%s) signed content (type=%s) does not contain any signed parts.",
                msgId, msgFrom, msgTo, ToolMimePartUtils.getContentType(signed.getContent())));
        }

        return signerCertMap;
    }

    private MimeBodyPart decrypt(ToolMimeMessageHelper msgHelper, String msgId, MailAddress msgFrom, MailAddress msgTo, DiscoveryTestcase testcase,
        DiscoveryTestcaseResult result) throws Exception {
        SMIMEEnveloped enveloped = null;

        if (testcase.hasCredentials()) {
            result.setExpectedDecryptionCredential(CollectionUtils.find(testcase.getTargetCredentials(), DiscoveryTestcaseCredentialValidPredicate.INSTANCE));
            enveloped = ToolSmimeUtils.getEnveloped(msgHelper);

            CredentialInfo credInfo;
            KeyInfo credKeyInfo;
            CertificateInfo credCertInfo;

            // noinspection ConstantConditions
            for (DiscoveryTestcaseCredential cred : testcase.getCredentials()) {
                // noinspection ConstantConditions
                if (!cred.hasCredentialInfo() || !(credInfo = cred.getCredentialInfo()).hasKeyDescriptor()
                    || !(credKeyInfo = credInfo.getKeyDescriptor()).hasPrivateKey() || !credInfo.hasCertificateDescriptor()
                    || !(credCertInfo = credInfo.getCertificateDescriptor()).hasCertificate()) {
                    continue;
                }

                MimeBodyPart decryptedBodyPart = ToolSmimeUtils.decrypt(msgHelper, enveloped, credKeyInfo.getPrivateKey(), credCertInfo.getCertificate());

                if (decryptedBodyPart != null) {
                    result.setDecryptionCredential(cred);

                    LOGGER.info(String.format("Decrypted enveloped mail MIME message (id=%s, from=%s, to=%s) enveloped content (type=%s).", msgId, msgFrom,
                        msgTo, ToolMimePartUtils.getContentType(enveloped.getEncryptedContent())));

                    return decryptedBodyPart;
                }
            }
        }

        // noinspection ConstantConditions
        throw new ToolSmimeException(String.format("Unable to decrypt enveloped mail MIME message (id=%s, from=%s, to=%s) enveloped content (type=%s).", msgId,
            msgFrom, msgTo, ToolMimePartUtils.getContentType(enveloped.getEncryptedContent())));
    }
}
