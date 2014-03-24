package gov.hhs.onc.dcdt.crypto.mail.utils;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.mail.MailCryptographyException;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.utils.ToolMailContentTypeUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.KeyTransRecipientInformation;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

public abstract class MailCryptographyUtils {
    private final static Properties MAIL_SESSION_PROPS = new Properties();
    private final static Session MAIL_SESSION = Session.getDefaultInstance(MAIL_SESSION_PROPS);

    private final static Logger LOGGER = Logger.getLogger(MailCryptographyUtils.class);

    public static Session getMailSession() {
        return MAIL_SESSION;
    }

    @SuppressWarnings("unchecked")
    public static Map<BigInteger, KeyTransRecipientInformation> getRecipients(SMIMEEnveloped envelopedMsg) throws MailCryptographyException {
        Collection<RecipientInformation> recipientsStore = (Collection<RecipientInformation>) envelopedMsg.getRecipientInfos().getRecipients();
        Map<BigInteger, KeyTransRecipientInformation> recipientsMap = new LinkedHashMap<>(recipientsStore.size());

        for (RecipientInformation recipient : recipientsStore) {
            if (!KeyTransRecipientInformation.class.isAssignableFrom(recipient.getClass())) {
                throw new MailCryptographyException(String.format("Recipient (class=%s) is not an instance of (class=%s).", ToolClassUtils.getName(recipient),
                    ToolClassUtils.getName(KeyTransRecipientInformation.class)));
            }
            recipientsMap.put(((KeyTransRecipientId) recipient.getRID()).getSerialNumber(), (KeyTransRecipientInformation) recipient);
        }

        return recipientsMap;
    }

    public static MimeMessage getMimeMessage(InputStream msgInStream) throws MailCryptographyException {
        try {
            MimeMessage mimeMessage = new MimeMessage(MailCryptographyUtils.getMailSession(), msgInStream);
            String contentTypeStr = mimeMessage.getContentType();

            try {
                if (!ToolMailContentTypeUtils.isEnvelopedData(MimeTypeUtils.parseMimeType(contentTypeStr))) {
                    throw new MailCryptographyException(String.format("Invalid Content-Type (%s), expected %s or %s.", contentTypeStr,
                        MailContentTypes.APP_PKCS7_MIME_ENV, MailContentTypes.APP_X_PKCS7_MIME_ENV));
                }

                return mimeMessage;
            } catch (InvalidMimeTypeException e) {
                throw new MailCryptographyException(String.format("Unable to parse content type string (%s) into class=%s.", contentTypeStr,
                    ToolClassUtils.getName(MimeType.class)));
            }
        } catch (MessagingException e) {
            throw new MailCryptographyException(String.format("Unable to convert input stream (class=%s) to message (class=%s).",
                ToolClassUtils.getName(msgInStream), ToolClassUtils.getName(MimeMessage.class)), e);
        }
    }

    public static SMIMEEnveloped getEnvelopedMessage(MimeMessage mimeMsg) throws MailCryptographyException {
        try {
            return new SMIMEEnveloped(mimeMsg);
        } catch (CMSException | MessagingException e) {
            throw new MailCryptographyException(String.format("Unable to get enveloped message from encrypted message (class=%s).",
                ToolClassUtils.getName(MimeMessage.class)), e);
        }
    }

    public static SMIMESigned getSignedMessageEntity(Multipart msgMultiPart) throws MailCryptographyException {
        String contentTypeStr = msgMultiPart.getContentType();

        try {
            MimeType contentType = MimeTypeUtils.parseMimeType(contentTypeStr);

            if (ToolMailContentTypeUtils.isMultipartSignature(contentType)) {
                try {
                    return new SMIMESigned((MimeMultipart) msgMultiPart);
                } catch (CMSException | MessagingException e) {
                    String errorMsg =
                        String.format("Unable to create signed message entity from multipart (class=%s) for content type %s",
                            ToolClassUtils.getName(Multipart.class), contentTypeStr);
                    LOGGER.debug(errorMsg);
                    throw new MailCryptographyException(errorMsg, e);
                }
            } else {
                String errorMsg =
                    String.format("Multipart (content type=%s) is not a multipart signature of content type %s or %s", contentTypeStr,
                        MailContentTypes.MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG, MailContentTypes.MULTIPART_SIGNED_PROTOCOL_X_PKCS7_SIG);
                LOGGER.debug(errorMsg);
                throw new MailCryptographyException(errorMsg);
            }
        } catch (InvalidMimeTypeException e) {
            throw new MailCryptographyException(String.format("Unable to parse content type string (%s) into class=%s.", contentTypeStr,
                ToolClassUtils.getName(MimeType.class)));
        }
    }

    @SuppressWarnings("unchecked")
    public static CertificateInfo validateSignature(SMIMESigned signedEntity) throws MailCryptographyException {
        Store certStore = signedEntity.getCertificates();

        for (SignerInformation signerInfo : (Collection<SignerInformation>) signedEntity.getSignerInfos().getSigners()) {
            for (X509CertificateHolder certHolder : (Collection<X509CertificateHolder>) certStore.getMatches(signerInfo.getSID())) {
                try {
                    if (signerInfo.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certHolder))) {
                        return new CertificateInfoImpl(new JcaX509CertificateConverter().getCertificate(certHolder));
                    }
                } catch (CMSException | OperatorCreationException | CertificateException ignored) {
                }
            }
        }

        throw new MailCryptographyException("Unable to validate signature.");
    }
}
