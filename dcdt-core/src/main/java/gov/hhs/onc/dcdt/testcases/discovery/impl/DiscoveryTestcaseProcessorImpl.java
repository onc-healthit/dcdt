package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.impl.ToolMessageImpl;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.crypto.ToolSmimeException;
import gov.hhs.onc.dcdt.mail.crypto.utils.ToolSmimeUtils;
import gov.hhs.onc.dcdt.mail.utils.ToolMimePartUtils;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseProcessor;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.mail.internet.MimeBodyPart;
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
        MailInfo mailInfo = submission.getMailInfo();
        String msgId = null;
        MailAddress msgFromAddr = null, msgToAddr = null;
        List<ToolMessage> procMsgs = new ArrayList<>();
        boolean procSuccess = true;

        try {
            msgId = mailInfo.getMessageId();
            msgFromAddr = mailInfo.getFrom();
            msgToAddr = mailInfo.getTo();

            if (testcase == null) {
                throw new ToolSmimeException(String.format("Unable to find a matching Discovery testcase for mail MIME message (id=%s, from=%s, to=%s).",
                    msgId, msgFromAddr, msgToAddr));
            } else if (testcase.isNegative()) {
                procMsgs.add(new ToolMessageImpl(ToolMessageLevel.ERROR, String.format(
                    "Matching Discovery testcase (name=%s) is negative; mail MIME message (id=%s, from=%s, to=%s) should not have been sent.",
                    testcase.getName(), msgId, msgFromAddr, msgToAddr)));
                procSuccess = false;
            }

            MimeBodyPart decryptedBodyPart = this.decrypt(mailInfo, msgId, msgFromAddr, msgToAddr, testcase, result);

            result.setSignerCertificateInfos(this.verifySigners(mailInfo, msgId, msgFromAddr, msgToAddr, decryptedBodyPart));
        } catch (Exception e) {
            procMsgs.add(new ToolMessageImpl(ToolMessageLevel.ERROR, e.getMessage()));
            procSuccess = false;
        }

        result.setProcessingMessages(procMsgs);
        result.setProcessingSuccess((procSuccess && (result.getDecryptionCredential() == result.getExpectedDecryptionCredential())));

        LOGGER.info(String.format("Processed (success=%s) Discovery testcase (name=%s) for mail MIME message (id=%s, from=%s, to=%s): [%s]",
            result.isSuccess(), ((testcase != null) ? testcase.getName() : null), msgId, msgFromAddr, msgToAddr,
            ToolStringUtils.joinDelimit(result.getMessages(), "; ")));

        return result;
    }

    private Map<SignerId, CertificateInfo> verifySigners(MailInfo mailInfo, String msgId, MailAddress msgFromAddr, MailAddress msgToAddr,
        MimeBodyPart decryptedBodyPart) throws Exception {
        SMIMESigned signed = ToolSmimeUtils.getSigned(mailInfo, decryptedBodyPart);

        LOGGER.info(String.format("Extracted mail MIME message (id=%s, from=%s, to=%s) signed content (type=%s).", msgId, msgFromAddr, msgToAddr,
            ToolMimePartUtils.getContentType(signed.getContent())));

        Map<SignerId, CertificateInfo> signerCertMap = ToolSmimeUtils.verifySignatures(signed);

        if (signerCertMap.isEmpty()) {
            throw new ToolSmimeException(String.format("Mail MIME message (id=%s, from=%s, to=%s) signed content (type=%s) does not contain any signed parts.",
                msgId, msgFromAddr, msgToAddr, ToolMimePartUtils.getContentType(signed.getContent())));
        }

        return signerCertMap;
    }

    private MimeBodyPart decrypt(MailInfo mailInfo, String msgId, MailAddress msgFromAddr, MailAddress msgToAddr, DiscoveryTestcase testcase,
        DiscoveryTestcaseResult result) throws Exception {
        SMIMEEnveloped enveloped = null;

        if (testcase.hasCredentials()) {
            result.setExpectedDecryptionCredential((testcase.hasTargetCredentials() ? testcase.getTargetCredentials().stream()
                .filter(DiscoveryTestcaseCredential::isValid).findFirst().orElse(null) : null));
            enveloped = ToolSmimeUtils.getEnveloped(mailInfo);

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

                MimeBodyPart decryptedBodyPart = ToolSmimeUtils.decrypt(mailInfo, enveloped, credKeyInfo.getPrivateKey(), credCertInfo.getCertificate());

                if (decryptedBodyPart != null) {
                    result.setDecryptionCredential(cred);

                    LOGGER.info(String.format("Decrypted enveloped mail MIME message (id=%s, from=%s, to=%s) enveloped content (type=%s).", msgId, msgFromAddr,
                        msgToAddr, ToolMimePartUtils.getContentType(enveloped.getEncryptedContent())));

                    return decryptedBodyPart;
                }
            }
        }

        // noinspection ConstantConditions
        throw new ToolSmimeException(String.format("Unable to decrypt enveloped mail MIME message (id=%s, from=%s, to=%s) enveloped content (type=%s).", msgId,
            msgFromAddr, msgToAddr, ToolMimePartUtils.getContentType(enveloped.getEncryptedContent())));
    }
}
