package gov.hhs.onc.dcdt.crypto.mail.decrypt;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.mail.utils.ToolMailCryptographyStringUtils;
import gov.hhs.onc.dcdt.mail.EmailInfo;
import gov.hhs.onc.dcdt.crypto.mail.MailCryptographyException;
import gov.hhs.onc.dcdt.crypto.mail.utils.MailCryptographyUtils;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
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

public abstract class MailDecryptor {
    private final static Logger LOGGER = Logger.getLogger(MailDecryptor.class);

    public static MimeMultipart decryptMail(MimeMessage mimeMsg, CredentialInfo credentialInfo) throws MailCryptographyException {
        // noinspection ConstantConditions
        return decryptMail(mimeMsg, credentialInfo.getKeyDescriptor().getPrivateKey(), credentialInfo.getCertificateDescriptor().getCertificate());
    }

    @SuppressWarnings("unchecked")
    public static MimeMultipart decryptMail(MimeMessage mimeMsg, PrivateKey key, X509Certificate cert) throws MailCryptographyException {
        SMIMEEnveloped envelopedMsg = MailCryptographyUtils.getEnvelopedMsg(mimeMsg);
        KeyTransRecipientId recipientId = new JceKeyTransRecipientId(cert);
        KeyTransRecipientInformation recipient = MailCryptographyUtils.getRecipients(envelopedMsg).get(recipientId.getSerialNumber());

        if (recipient == null) {
            throw new MailDecryptionException(String.format(
                "Enveloped mail does not contain a matching recipient for certificate (subject=%s), serialNum=%s): %s.", cert.getSubjectX500Principal(),
                ToolMailCryptographyStringUtils.serialNumToString(cert.getSerialNumber()), ToolMailCryptographyStringUtils.envelopedMsgToString(envelopedMsg)));
        }

        return decryptMail(key, cert, envelopedMsg, recipient);
    }

    public static MimeMultipart decryptMail(PrivateKey key, X509Certificate cert, SMIMEEnveloped envelopedMsg, KeyTransRecipientInformation recipient)
        throws MailDecryptionException {
        String errorMsg =
            String.format("Unable to decrypt enveloped message content for mail recipient (%s) using private key for subject (dn=%s): %s.",
                ToolMailCryptographyStringUtils.recipientsToString(recipient), cert.getSubjectX500Principal(),
                ToolMailCryptographyStringUtils.envelopedMsgToString(envelopedMsg));

        try {
            byte[] decryptedContent = recipient.getContent(new JceKeyTransEnvelopedRecipient(key));

            if (ArrayUtils.isEmpty(decryptedContent)) {
                throw new MailDecryptionException(errorMsg);
            }

            MimeMultipart msgMultiPart = (MimeMultipart) SMIMEUtil.toMimeBodyPart(decryptedContent).getContent();
            LOGGER.debug(String.format("Decrypted enveloped message for mail recipient (%s) using private key for subject (dn=%s): numMsgParts=%d.",
                ToolMailCryptographyStringUtils.recipientsToString(recipient), cert.getSubjectX500Principal(), msgMultiPart.getCount()));

            return msgMultiPart;
        } catch (CMSException | IOException | MessagingException | SMIMEException e) {
            throw new MailDecryptionException(errorMsg);
        }
    }

    public static boolean decryptMail(EmailInfo emailInfo, ToolStrBuilder decryptionErrorBuilder, MimeMessage origMimeMessage,
        DiscoveryTestcaseCredential cred, Set<CertificateValidator> certValidators) {
        CredentialInfo credInfo = cred.getCredentialInfo();

        if (credInfo != null) {
            MimeMultipart multipartMsg = null;

            try {
                multipartMsg = MailDecryptor.decryptMail(origMimeMessage, credInfo);
            } catch (MailCryptographyException e) {
                decryptionErrorBuilder.appendWithDelimiter(e.getMessage(), StringUtils.LF);
            }

            if (multipartMsg != null) {
                emailInfo.getResultInfo().setCredentialFound(cred);
                processDecryptedMessage(emailInfo, multipartMsg, decryptionErrorBuilder, certValidators);
                return true;
            }
        }

        return false;
    }

    public static void processDecryptedMessage(EmailInfo emailInfo, MimeMultipart multipartMsg, ToolStrBuilder decryptionErrorBuilder,
        Set<CertificateValidator> certValidators) {
        MimeMessage decryptedMsg = null;

        try {
            decryptedMsg = MailCryptographyUtils.findMessagePart(multipartMsg, emailInfo.getEncryptedMessage());
            emailInfo.setDecryptedMessage(decryptedMsg);
        } catch (MailCryptographyException e) {
            decryptionErrorBuilder.appendWithDelimiter(e.getMessage(), StringUtils.LF);
        }
        emailInfo.setDecryptedMessage(decryptedMsg);

        try {
            CertificateInfo certInfo = MailCryptographyUtils.validateSignature(MailCryptographyUtils.findMailSignature(multipartMsg));
            ToolTestcaseCertificateResultType certStatus =
                CertificateUtils.validateCertificate(certInfo, new MailAddressImpl(emailInfo.getFromAddress()), certValidators);

            if (certStatus != ToolTestcaseCertificateResultType.VALID_CERT) {
                decryptionErrorBuilder.appendWithDelimiter(String.format("Signer certificate in message signature was invalid (error: %s).", certStatus),
                    StringUtils.LF);
            }
        } catch (MailCryptographyException e) {
            decryptionErrorBuilder.appendWithDelimiter(e.getMessage(), StringUtils.LF);
        }
    }
}
