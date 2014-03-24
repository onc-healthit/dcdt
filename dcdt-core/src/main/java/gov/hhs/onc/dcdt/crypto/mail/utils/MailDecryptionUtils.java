package gov.hhs.onc.dcdt.crypto.mail.utils;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.mail.ToolMailDecryptionException;
import gov.hhs.onc.dcdt.crypto.mail.ToolMailCryptographyException;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.testcases.utils.ToolTestcaseCertificateUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.KeyTransRecipientInformation;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMEUtil;

public abstract class MailDecryptionUtils {
    private final static Logger LOGGER = Logger.getLogger(MailDecryptionUtils.class);

    public static MimeMultipart decryptMail(MimeMessage mimeMsg, CredentialInfo credentialInfo) throws MessagingException {
        // noinspection ConstantConditions
        return decryptMail(mimeMsg, credentialInfo.getKeyDescriptor().getPrivateKey(), credentialInfo.getCertificateDescriptor().getCertificate());
    }

    @SuppressWarnings("unchecked")
    public static MimeMultipart decryptMail(MimeMessage mimeMsg, PrivateKey key, X509Certificate cert) throws MessagingException {
        SMIMEEnveloped envelopedMsg = MailCryptographyUtils.getEnvelopedMessage(mimeMsg);
        KeyTransRecipientId recipientId = new JceKeyTransRecipientId(cert);
        KeyTransRecipientInformation recipient = MailCryptographyUtils.getRecipients(envelopedMsg).get(recipientId.getSerialNumber());

        if (recipient == null) {
            throw new ToolMailDecryptionException(String.format(
                "Enveloped mail does not contain a matching recipient for certificate (subject=%s), serialNum=%s): %s.", cert.getSubjectX500Principal(),
                ToolMailCryptographyStringUtils.serialNumToString(cert.getSerialNumber()), ToolMailCryptographyStringUtils.envelopedMsgToString(envelopedMsg)));
        }

        return decryptMail(key, cert, envelopedMsg, recipient);
    }

    public static MimeMultipart decryptMail(PrivateKey key, X509Certificate cert, SMIMEEnveloped envelopedMsg, KeyTransRecipientInformation recipient)
        throws MessagingException {
        String errorMsg =
            String.format("Unable to decrypt enveloped message content for mail recipient (%s) using private key for subject (dn=%s): %s.",
                ToolMailCryptographyStringUtils.recipientsToString(recipient), cert.getSubjectX500Principal(),
                ToolMailCryptographyStringUtils.envelopedMsgToString(envelopedMsg));

        try {
            byte[] decryptedContent = recipient.getContent(new JceKeyTransEnvelopedRecipient(key));

            if (ArrayUtils.isEmpty(decryptedContent)) {
                throw new ToolMailDecryptionException(errorMsg);
            }

            MimeMultipart msgMultiPart = (MimeMultipart) SMIMEUtil.toMimeBodyPart(decryptedContent).getContent();
            LOGGER.debug(String.format("Decrypted enveloped message for mail recipient (%s) using private key for subject (dn=%s): numMsgParts=%d.",
                ToolMailCryptographyStringUtils.recipientsToString(recipient), cert.getSubjectX500Principal(), msgMultiPart.getCount()));

            return msgMultiPart;
        } catch (CMSException | IOException | MessagingException | SMIMEException e) {
            LOGGER.debug(String.format("Unable to decrypt enveloped message for mail recipient (%s) using private key for subject (dn=%s).",
                ToolMailCryptographyStringUtils.recipientsToString(recipient), cert.getSubjectX500Principal()));
            throw new ToolMailDecryptionException(errorMsg);
        }
    }

    public static boolean decryptMail(MailInfo mailInfo, ToolStrBuilder decryptionErrorBuilder, DiscoveryTestcaseCredential cred,
        Set<CertificateValidator> certValidators) throws MessagingException {
        CredentialInfo credInfo = cred.getCredentialInfo();

        if (credInfo != null) {
            MimeMultipart multipartMsg = null;

            try {
                // noinspection ConstantConditions
                multipartMsg = decryptMail(mailInfo.getEncryptedMessageHelper().getMimeMessage(), credInfo);
            } catch (ToolMailCryptographyException e) {
                decryptionErrorBuilder.appendWithDelimiter(e.getMessage(), StringUtils.LF);
            }

            if (multipartMsg != null) {
                processMessageSignature(mailInfo.getFrom(), multipartMsg, decryptionErrorBuilder, certValidators);
                return true;
            }
        }

        return false;
    }

    public static void processMessageSignature(MailAddress mailAddr, MimeMultipart multipartMsg, ToolStrBuilder decryptionErrorBuilder,
        Set<CertificateValidator> certValidators) {
        try {
            CertificateInfo certInfo = MailCryptographyUtils.validateSignature(MailCryptographyUtils.getSignedMessageEntity(multipartMsg));
            ToolTestcaseCertificateResultType certStatus = ToolTestcaseCertificateUtils.validateCertificate(certInfo, mailAddr, certValidators);

            if (certStatus != ToolTestcaseCertificateResultType.VALID_CERT) {
                decryptionErrorBuilder.appendWithDelimiter(String.format("Signer certificate in message signature was invalid (error: %s).", certStatus),
                    StringUtils.LF);
            }
        } catch (MessagingException e) {
            decryptionErrorBuilder.appendWithDelimiter(e.getMessage(), StringUtils.LF);
        }
    }
}
